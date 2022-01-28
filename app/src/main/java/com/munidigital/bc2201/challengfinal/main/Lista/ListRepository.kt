package com.munidigital.bc2201.challengfinal.main.Lista

import com.munidigital.bc2201.challengfinal.Equipo
import com.munidigital.bc2201.challengfinal.api.EquipoJsonResponse
import com.munidigital.bc2201.challengfinal.api.service
import com.munidigital.bc2201.challengfinal.database.EquiposDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListRepository (private val database: EquiposDatabase){

    suspend fun obtenerEquipos() : MutableList<Equipo>{
        return withContext(Dispatchers.IO){

            val equipoJsonResponse = service.getEquipos()
            val listaEquipos = parseEquipoResultado(equipoJsonResponse)

            database.equiposDao.insertAll(listaEquipos)

            recuperarEquiposDatabase()

            listaEquipos
        }
    }

    suspend fun recuperarEquiposDatabase(): MutableList<Equipo> {
        return withContext(Dispatchers.IO) {
            database.equiposDao.getEquipos()
        }
    }

    suspend fun updateEquipo(equipo: Equipo){
        withContext(Dispatchers.IO){
            database.equiposDao.updateEquipo(equipo)
        }
    }


    private fun parseEquipoResultado(equipoJsonResponse: EquipoJsonResponse): MutableList<Equipo> {
    val listaEquipos = mutableListOf<Equipo>()
    val teams = equipoJsonResponse.teams

        for (team in teams){
            val id = team.idTeam
            val nombre = team.strTeam
            val nombreAlt = team.strAlternate
            val anioFundado = team.intFormedYear
            val liga1 = team.strLeague
            val idLiga1 = team.idLeague
            val liga2 = team.strLeague2
            val idLiga2 = team.idLeague2
            val estadio = team.strStadium
            val direccion = team.strStadiumLocation
            val capacidad = team.intStadiumCapacity
            val descripcion = team.strDescriptionEN
            val web = team.strWebsite
            val imagen = team.strTeamBadge
            val favorito = false

            listaEquipos.add(Equipo(id, nombre,nombreAlt, anioFundado, liga1, idLiga1, liga2, idLiga2, estadio, direccion,
                capacidad, descripcion, web, imagen, favorito ))
        }

        return listaEquipos
    }
}