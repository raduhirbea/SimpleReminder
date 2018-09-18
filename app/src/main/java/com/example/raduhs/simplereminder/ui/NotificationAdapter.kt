package com.example.raduhs.simplereminder.ui

import android.app.TimePickerDialog
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.raduhs.simplereminder.R
import com.example.raduhs.simplereminder.data.Notification
import com.example.raduhs.simplereminder.work.PeriodicJobWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import android.arch.lifecycle.Observer

class NotificationAdapter(val owner : LifecycleOwner, val userList: ArrayList<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = userList[position]
        holder?.txtName?.text = notification.message
        holder?.txtTime?.text = calculateTime(notification.hour, notification.minute)

        holder?.txtTime?.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                notification.hour = hour
                notification.minute = minute
                holder?.txtTime?.text = calculateTime(hour, minute)
            }
            TimePickerDialog(holder.context, timeSetListener, notification.hour, notification.minute, true).show()
        }

        WorkManager.getInstance()!!.getStatusesByTag(notification.notificationId.toString())
                .observe(owner, Observer { workStatus ->
                    if (workStatus != null) {
                        var running = false
                        for (ws in workStatus) {
//                            Log.d("TEST", ws.id.toString())
/*                            Log.d("TEST", ws.tags.toString())
                            Log.d("TEST", ws.state.toString())*/

                            running = !ws.state.isFinished
                            Log.d("TEST", notification.notificationId.toString() + " running" + running)
                            holder?.statusSwitch?.tag = true
                            holder?.statusSwitch?.isChecked = running
                            holder.container.setBackgroundColor(if (running) Color.GREEN else Color.RED)
                            holder?.statusSwitch?.tag = false
                        }
                    }
                })


        holder?.statusSwitch?.tag = false
        holder?.statusSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (holder?.statusSwitch?.tag==true) return@setOnCheckedChangeListener

            holder.container.setBackgroundColor(if (isChecked) Color.GREEN else Color.RED)

            if (isChecked) {
                enqueWorkers(notification)
            } else {
                WorkManager.getInstance().cancelAllWorkByTag(notification.notificationId.toString())
            }
        }
    }

    private fun calculateTime(hour: Int, minute: Int): String {
        val cal = Calendar.getInstance()

        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        return SimpleDateFormat("HH:mm").format(cal.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v);
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context: Context = itemView.context
        val txtName = itemView.findViewById<TextView>(R.id.message)
        val txtTime = itemView.findViewById<TextView>(R.id.txtTime)
        val statusSwitch = itemView.findViewById<Switch>(R.id.statusSwitch)
        val container = itemView.findViewById<View>(R.id.container)
    }




    private fun enqueWorkers(notification : Notification) {
            val myData = Data.Builder()
                    .putString("MESSAGE", notification.message)
                    .putLong("INTERVAL", notification.interval)
                    .putInt("ID", notification.notificationId)
                    .build()

            val work = OneTimeWorkRequest.Builder(PeriodicJobWorker::class.java)
                    .setInputData(myData)
                    .addTag(notification.notificationId.toString())
                    .setInitialDelay(calculateDelay(notification.hour, notification.minute), TimeUnit.SECONDS)
                    .build()
            WorkManager.getInstance().enqueue(work)
    }

    private fun calculateDelay(hour : Int, minute : Int): Long {
        val cal = Calendar.getInstance()
        val curHour = cal.get(Calendar.HOUR_OF_DAY)
        if (curHour > hour) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)

        val delay = (cal.timeInMillis-Calendar.getInstance().timeInMillis)/1000

        Log.d("TEST", "delay in hours" + delay/60/60)


        return delay
    }

}
