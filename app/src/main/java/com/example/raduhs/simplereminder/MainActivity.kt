package com.example.raduhs.simplereminder

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.raduhs.simplereminder.work.PeriodicJobWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        fab.setOnClickListener { view ->
            scheduleNotification()
            Snackbar.make(view, "Worker scheduled", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun scheduleNotification() {

        enqueWorker("Time to feed the fishes", 24, calculateDelay(12, 0), 101)
        enqueWorker("Time for sport", 24, calculateDelay(14, 0), 101)
        enqueWorker("Time to go for a walk", 24, calculateDelay(18, 0), 102)

    }

    private fun enqueWorker(message: String, interval: Long, delay: Long, notificationId: Int) {
        val myData = Data.Builder()
                .putString("MESSAGE", message)
                .putLong("INTERVAL", interval)
                .putInt("ID", notificationId)
                .build()

        val work = OneTimeWorkRequest.Builder(PeriodicJobWorker::class.java)
                .setInputData(myData)
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .build()
        WorkManager.getInstance().enqueue(work)
    }

    private fun calculateDelay(hour : Int, minute : Int): Long {
        val cal = Calendar.getInstance()
        val curHour = cal.get(HOUR_OF_DAY)
        if (curHour > hour) {
            cal.add(DAY_OF_MONTH, 1)
        }
        cal.set(HOUR_OF_DAY, hour)
        cal.set(MINUTE, minute)

        val delay = (cal.timeInMillis-Calendar.getInstance().timeInMillis)/1000

        Log.d("TEST", "delay in minutes" + delay/60)

        return delay
    }


}

