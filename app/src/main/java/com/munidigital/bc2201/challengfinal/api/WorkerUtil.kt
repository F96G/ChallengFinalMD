package com.munidigital.bc2201.challengfinal.api

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerUtil {
    fun scheduleSync(context: Context){
        val constrains = Constraints.Builder()
            //Solo se conecta si el celuar esta conectado a internet
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorkManager>(
            1, TimeUnit.HOURS)
            .setConstraints(constrains).build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                SyncWorkManager.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, syncRequest)
    }
}