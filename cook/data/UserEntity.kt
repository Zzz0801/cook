package com.zjgsu.cook.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val email: String,
    val username: String,
    val avatarPath: String?,
    val password: String
)
