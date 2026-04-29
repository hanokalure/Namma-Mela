package com.nammamela.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plays")
data class Play(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val duration: String,
    val description: String,
    val genre: String,
    val posterUrl: String? = null,
    val rating: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val isActive: Boolean = true // True for currently running show
)
