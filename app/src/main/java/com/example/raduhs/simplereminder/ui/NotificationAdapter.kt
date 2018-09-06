package com.example.raduhs.simplereminder.ui

import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.raduhs.simplereminder.R
import com.example.raduhs.simplereminder.data.Notification
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(val userList: ArrayList<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

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
    }

}
