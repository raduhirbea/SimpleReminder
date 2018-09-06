package com.example.raduhs.simplereminder.data

data class Notification(val notificationId: Int, val message: String, val interval: Long = 24, var hour : Int, var minute : Int)
