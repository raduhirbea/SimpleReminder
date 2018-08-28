package com.example.raduhs.simplereminder.work.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.raduhs.simplereminder.R
import com.example.raduhs.simplereminder.work.data.Notification

class NotificationAdapter(val userList: ArrayList<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = userList[position]
        holder?.txtName?.text = notification.message
        holder?.txtTime?.text = calculateTime(notification.hour, notification.minute)
    }

    private fun calculateTime(hour: Int, minute: Int): String {
        return if (minute < 10) {
            "$hour : 0$minute"
        } else {
            "$hour : $minute"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v);
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById<TextView>(R.id.message)
        val txtTime = itemView.findViewById<TextView>(R.id.txtTime)
    }

}
