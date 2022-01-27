package com.munidigital.bc2201.challengfinal.database

import androidx.room.*
import com.munidigital.bc2201.challengfinal.Equipo

@Dao
interface EquiposDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(eqList: MutableList<Equipo>)


    @Query("SELECT * FROM equipos")
    fun getEquipos():MutableList<Equipo>

    @Update
    fun updateEquipo(equipo:Equipo)
}
