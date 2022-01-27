package com.munidigital.bc2201.challengfinal.api

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.munidigital.bc2201.challengfinal.database.getDatabase
import com.munidigital.bc2201.challengfinal.main.MainRepository

class SyncWorkManager(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    companion object{
        const val WORK_NAME = "SyncWorkManager"
    }

    private val database = getDatabase(appContext)
    private val repositorio = MainRepository(database)


    override suspend fun doWork(): Result {
        repositorio.recuperarEquiposDatabase()
        //TODO ver si es de data base o no

        return Result.success()
    }
}