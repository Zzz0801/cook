package com.zjgsu.cook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("UPDATE users SET avatarPath = :avatarUri WHERE userId = :userId")
    fun updateAvatar(userId: Int, avatarUri: String)

    @Query("SELECT avatarPath FROM users WHERE userId = :userId")
    fun getAvatarUri(userId: Int): String?

    @Query("UPDATE users SET username = :newUsername WHERE userId = :userId")
    suspend fun updateUsername(userId: Int, newUsername: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)


}