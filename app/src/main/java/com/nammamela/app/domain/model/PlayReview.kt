package com.nammamela.app.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "play_reviews",
    indices = [Index(value = ["playId", "userId"], unique = true)]
)
data class PlayReview(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playId: Int,
    val userId: Int,
    val rating: Int,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
