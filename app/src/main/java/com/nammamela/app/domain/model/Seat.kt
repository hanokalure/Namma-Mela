package com.nammamela.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SeatStatus {
    AVAILABLE, BOOKED, SELECTED
}

@Entity(tableName = "seats")
data class Seat(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playId: Int,
    val row: String, // A, B, C...
    val column: Int, // 1, 2, 3...
    val status: SeatStatus = SeatStatus.AVAILABLE
)
