package com.nammamela.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    USER, ADMIN
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val handle: String,
    val email: String,
    val password: String = "",
    val role: UserRole = UserRole.USER,
    val imageUrl: String? = null,
    
    // Admin / Manager Specific Fields
    val companyName: String? = null,
    val location: String? = null,
    val phone: String? = null
)
