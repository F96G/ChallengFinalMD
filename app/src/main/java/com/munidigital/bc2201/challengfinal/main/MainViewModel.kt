package com.munidigital.bc2201.challengfinal.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.munidigital.bc2201.challengfinal.Equipo
import com.munidigital.bc2201.challengfinal.api.ApiResposeStatus
import com.munidigital.bc2201.challengfinal.database.getDatabase
import com.munidigital.bc2201.challengfinal.main.Lista.ListRepository
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.*


class MainViewModel(application: Application): AndroidViewModel(application) {
    private var _equiposView = MutableLiveData<MutableList<Equipo>>()
    val equiposView: LiveData<MutableList<Equipo>> get() =_equiposView

    private var _status = MutableLiveData<ApiResposeStatus>()
    val status:LiveData<ApiResposeStatus> get() = _status

    private var soyFavorito = false
    private var saveList = mutableListOf<Equipo>()
    private val database = getDatabase(application)
    private val repositorio = ListRepository(database)

    init {
        cargarEquiposDatabase()
    }

    fun cargarBusqueda(newText: String?){
        val tempList = mutableListOf<Equipo>()

        newText?.let {
            val textoBusqueda =  it.lowercase(Locale.getDefault())

            if (textoBusqueda.isNotEmpty()){
                for (equipo in saveList)
                    if (equipo.nombre.lowercase(Locale.getDefault()).contains(textoBusqueda))
                        tempList.add(equipo)
            }else {
                tempList.addAll(saveList)
            }

            _equiposView.value = tempList
        }
    }

    fun setearFavoritos(){
        if (soyFavorito){//Si era facorito vuelvo a cargar
            cargarEquiposDatabase()
        }else{//Si no era favorito cargo los favoritos
            val tempList = mutableListOf<Equipo>()
            for (equipo in saveList){
                if (equipo.favorito)
                    tempList.add(equipo)
            }
            _equiposView.value = tempList
            saveList = tempList
            soyFavorito = true
        }
    }

    fun seleccionarFavorito(e:Equipo){
        var equipoTemp = e
        equipoTemp.favorito = !equipoTemp.favorito
        viewModelScope.launch {
            repositorio.updateEquipo(equipoTemp)
        }
        cargarEquiposDatabase()
    }

    fun cargarEquiposDatabase() {
        viewModelScope.launch {
            saveList = repositorio.recuperarEquiposDatabase()
                if (saveList.isEmpty())
                    cargarEquipos()
            _equiposView.value = saveList
        }
        //Nunca voy a cargar el equipo completo y ser favorito
        soyFavorito = false
    }

    private fun cargarEquipos() {
        viewModelScope.launch {
            try {
                _status.value = ApiResposeStatus.LOADING
                saveList = repositorio.obtenerEquipos()
                _status.value = ApiResposeStatus.DONE
                _equiposView.value = saveList
            }catch (e: UnknownHostException){
                Log.d(MainViewModel::class.java.simpleName,"No internet connection", e )
                _status.value = ApiResposeStatus.ERROR
            }
        }
    }

}