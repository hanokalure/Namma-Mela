package com.nammamela.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val username: String,
    val userHandle: String,
    val content: String,
    val timestamp: Long,
    val likes: Int = 0,
    val fires: Int = 0,
    val replies: Int = 0,
    val imageUrl: String? = null
)
