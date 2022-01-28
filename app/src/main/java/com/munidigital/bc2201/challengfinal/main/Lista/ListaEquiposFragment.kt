package com.munidigital.bc2201.challengfinal.main.Lista

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.munidigital.bc2201.challengfinal.EquipoAdapter
import com.munidigital.bc2201.challengfinal.R
import com.munidigital.bc2201.challengfinal.api.ApiResposeStatus
import com.munidigital.bc2201.challengfinal.api.WorkerUtil
import com.munidigital.bc2201.challengfinal.login.Logueo.LogActivity
import com.munidigital.bc2201.challengfinal.login.LoginViewModel
import com.munidigital.bc2201.challengfinal.main.MainViewModel

class ListaEquiposFragment : Fragment() {

    private lateinit var adapter: EquipoAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_lista_equipos, container, false)
        WorkerUtil.scheduleSync(requireActivity())
        //Habilita cambiar la toolbar
        setHasOptionsMenu(true)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        adapter = EquipoAdapter()

        val listaEquipos = rootView.findViewById<RecyclerView>(R.id.rvEquipos)
        val progressBar = rootView.findViewById<ProgressBar>(R.id.pbList)
        val tvError = rootView.findViewById<TextView>(R.id.tvErrorCarga)
        val btRecargar = rootView.findViewById<Button>(R.id.btRecargar)

        listaEquipos.layoutManager = LinearLayoutManager(requireActivity())
        listaEquipos.adapter = adapter

        mainViewModel.equiposView.observe(requireActivity()){
            adapter.submitList(it)
        }


        mainViewModel.status.observe(requireActivity()){
            when(it){
                (ApiResposeStatus.DONE)->{//Si cargo la lista
                    progressBar.visibility = View.GONE
                    listaEquipos.visibility = View.VISIBLE
                    tvError.visibility = View.GONE
                    btRecargar.visibility = View.GONE
                }
                (ApiResposeStatus.LOADING)->{//Si esta cargando la lista
                    progressBar.visibility = View.VISIBLE
                    listaEquipos.visibility = View.GONE
                    tvError.visibility = View.GONE
                    btRecargar.visibility = View.GONE
                }
                (ApiResposeStatus.ERROR)->{//Si hubo un error
                    progressBar.visibility = View.GONE
                    listaEquipos.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    btRecargar.visibility = View.VISIBLE
                }

            }
        }

        adapter.onItemClickListener = {
            val action = ListaEquiposFragmentDirections.actionListaEquiposFragmentToDatoFragment(it)
            findNavController().navigate(action)
        }

        btRecargar.setOnClickListener {
            mainViewModel.cargarEquiposDatabase()
        }


        return rootView
    }

    //Si se vulve atras de un fragment se muestre toda la lista
    override fun onResume() {
        mainViewModel.cargarEquiposDatabase()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_lista, menu)

        //Setea el boton de buscar
        val item = menu.findItem(R.id.iBuscar)
        val searchView = item?.actionView as SearchView
        searchView.queryHint = "Buscar equipo..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainViewModel.cargarBusqueda(newText)
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.iLogout->{
                val logViewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
                logViewModel.logout()
                val intent = Intent(requireActivity(), LogActivity::class.java )
                startActivity(intent)
                requireActivity().finish()
            }

            R.id.iFavoritoLista->{
                mainViewModel.setearFavoritos()
            }

        }
        return super.onOptionsItemSelected(item)
    }



}