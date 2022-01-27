package com.munidigital.bc2201.challengfinal.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface EquipoApiServer {

    @GET("search_all_teams.php?s=Soccer&c=Argentina")
    suspend fun getEquipos(): EquipoJsonResponse
}


    private var retrofit = Retrofit.Builder()
        .baseUrl("https://www.thesportsdb.com/api/v1/json/2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    var service: EquipoApiServer = retrofit.create<EquipoApiServer>(EquipoApiServer::class.java)