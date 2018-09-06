package com.example.raduhs.simplereminder.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.work.Worker
import com.example.raduhs.simplereminder.MainActivity

const val MESSAGE = "MESSAGE"
const val ID = "ID"

class JobWorker : Worker() {

    private var notificationManager: NotificationManager? = null
    private val channelID = "com.example.raduhs.simplereminder"


    override fun doWork(): Worker.Result {
        triggerNotification()

        // Indicate success or failure with your return value:
        return Worker.Result.SUCCESS
    }

    private fun triggerNotification() {
        notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID,"Reminder Demo", "Reminder Demo Channel")
        sendNotification()
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {

        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        notificationManager?.createNotificationChannel(channel)
    }

    private fun sendNotification() {
        val GROUP_KEY_NOTIFY = "group_key_notify"

        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = Notification.Builder(applicationContext, channelID)
                .setContentTitle("Notification")
                .setContentText(inputData.getString(MESSAGE))
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelID)
                .setGroup(GROUP_KEY_NOTIFY)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        notificationManager?.notify(inputData.getInt(ID, 0), notification)
    }
}