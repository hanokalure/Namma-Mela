package com.nammamela.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val playId: Int,
    val seats: String, // Comma separated e.g. "A1,A2"
    val totalPrice: Double,
    val timestamp: Long
)
