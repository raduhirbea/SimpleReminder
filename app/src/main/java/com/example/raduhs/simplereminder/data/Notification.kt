package com.example.raduhs.simplereminder.data

data class Notification(val notificationId: Int, val message: String, val interval: Long = 24, val hour : Int, val minute : Int)
