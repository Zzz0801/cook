package com.zjgsu.cook.data

import androidx.room.*
import com.zjgsu.cook.data.FoodItemEntity

@Dao
interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(item: FoodItemEntity)

    @Query("SELECT * FROM food_items WHERE userId = :userId")
    suspend fun getUserFoodItems(userId: Int): List<FoodItemEntity>

    @Delete
    suspend fun deleteFoodItem(item: FoodItemEntity)

    @Query("DELETE FROM food_items")
    suspend fun clearAll()


}