package com.example.raduhs.simplereminder

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import androidx.work.WorkManager
import com.example.raduhs.simplereminder.data.Notification
import com.example.raduhs.simplereminder.ui.NotificationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val notifications = ArrayList<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        loadData()

        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = NotificationAdapter(this, notifications)

        fab.setOnClickListener { view ->
            WorkManager.getInstance().cancelAllWork();
            Snackbar.make(view, "Canceling workers", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }

    private fun loadData() {
        notifications.add(Notification(101, "Time to feed the fishes", 24, 12, 0))
        notifications.add(Notification(102, "Time for sport", 24, 14, 0))
        notifications.add(Notification(103, "Time to go for a walk", 24, 18, 0))
        notifications.add(Notification(104, "Prepare for bed", 24, 23, 0))
    }

}

