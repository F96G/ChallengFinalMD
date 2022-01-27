package com.munidigital.bc2201.challengfinal

import android.os.Parcelable
import android.text.BoringLayout
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "equipos")
data class Equipo(@PrimaryKey val id:String, val nombre:String, val nombreAlt:String?, val anioFundado:Int, val liga1:String, val idLiga1:Int?,
                  val liga2:String?, val idLiga2:Int?, val estadio:String, val direccion:String, val capacidad:Int, val descripcion:String?,
                  val web:String, val imagen:String, var favorito:Boolean) : Parcelable
