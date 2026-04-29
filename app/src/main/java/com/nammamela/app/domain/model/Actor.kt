package com.nammamela.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actors")
data class Actor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playId: Int, // Associated play
    val name: String,
    val role: String,
    val imageUrl: String? = null,
    val category: String // "Hero", "Heroine", "Comedian", etc.
)
