package com.munidigital.bc2201.challengfinal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.munidigital.bc2201.challengfinal.Equipo


@Database(entities = [Equipo::class], version = 3)
abstract class EquiposDatabase: RoomDatabase(){
    abstract val equiposDao: EquiposDao
}


//Este es un singleton, siempre se hace igual
private lateinit var INSTANCE: EquiposDatabase


fun getDatabase(context: Context): EquiposDatabase {
    synchronized(EquiposDatabase::class.java) {
        if (!::INSTANCE.isInitialized)
            INSTANCE = Room.databaseBuilder(context.applicationContext, EquiposDatabase::class.java,"equipo_db").fallbackToDestructiveMigration().build()
    }
    return INSTANCE
}