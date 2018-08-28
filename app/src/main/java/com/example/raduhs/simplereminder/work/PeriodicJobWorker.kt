package com.example.raduhs.simplereminder.work

import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import java.util.concurrent.TimeUnit

class PeriodicJobWorker : Worker() {

    override fun doWork(): Worker.Result {
        val myData = Data.Builder()
                .putString("MESSAGE", inputData.getString(MESSAGE))
                .putInt("ID", inputData.getInt(ID, 0))
                .build()


        val compressionWork = PeriodicWorkRequest.Builder(JobWorker::class.java, inputData.getLong("INTERVAL", 0), TimeUnit.HOURS)
                .setInputData(myData)
                .build()
        WorkManager.getInstance().enqueue(compressionWork)

        return Worker.Result.SUCCESS
    }

}