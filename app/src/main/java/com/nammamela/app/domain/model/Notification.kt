package com.nammamela.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val message: String,
    val type: String, // "BOOKING", "INFO", "ALERT"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
