package com.zjgsu.cook

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zjgsu.cook.data.AppDatabase
import com.zjgsu.cook.data.FoodItemEntity
import com.zjgsu.cook.data.UserEntity
import com.zjgsu.cook.data.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val foodDao = AppDatabase.getDatabase(application).foodItemDao()
    private val dao = AppDatabase.getDatabase(application).foodItemDao()
    private val db = AppDatabase.getDatabase(application)
    private val userDao = db.userDao()
    private val _foodItems = MutableLiveData<List<FoodItemEntity>>()
    val foodItems: LiveData<List<FoodItemEntity>> get() = _foodItems
    private val userId = getUserIdFromStorage()

    init {
        if (userId == -1) {
            Log.e("FoodViewModel", "用户未登录")
        } else {
            Log.d("FoodViewModel", "当前用户ID: $userId")
            ensureUserExists(userId)
        }
    }

    // 检查用户是否存在，如果不存在则插入一个默认用户
    private fun ensureUserExists(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = userDao.getUserById(userId)
            if (existingUser == null) {
                val newUser = UserEntity(
                    userId = userId,
                    email = "test$userId@example.com",
                    username = "默认用户$userId",
                    password = "123456",
                    avatarPath = null
                )
                userDao.insertUser(newUser)
                Log.d("FoodViewModel", "插入默认用户 userId = $userId")
            } else {
                Log.d("FoodViewModel", "用户 userId = $userId 已存在")
            }
        }
    }

    private fun getUserIdFromStorage(): Int {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1)
    }

    private val _recentFoods = MutableLiveData<List<FoodItemEntity>>()
    val recentFoods: LiveData<List<FoodItemEntity>> get() = _recentFoods

    private val _nearExpiryFoods = MutableLiveData<List<FoodItemEntity>>()
    val nearExpiryFoods: LiveData<List<FoodItemEntity>> get() = _nearExpiryFoods

    private val _expiredFoods = MutableLiveData<List<FoodItemEntity>>()
    val expiredFoods: LiveData<List<FoodItemEntity>> get() = _expiredFoods

    fun deleteFoodItem(item: FoodItem) {
        viewModelScope.launch {
            foodDao.deleteFoodItem(item.toEntity(userId))
            loadFoodItems()
        }
    }

    fun loadFoodItems() {
        viewModelScope.launch {
            val allItems = dao.getUserFoodItems(userId)
            _foodItems.value = allItems
            val user = userDao.getUserById(userId)
            Log.d("FoodViewModel", "userDao.getUserById($userId) 结果: $user")

            val today = LocalDate.now()

            _recentFoods.value = allItems.sortedByDescending { it.addedTime }.take(10)

            _nearExpiryFoods.value = allItems.filter {
                val expiryDate = try {
                    it.expiryDate?.let { dateStr -> LocalDate.parse(dateStr) }
                } catch (e: Exception) {
                    null
                }

                val daysLeft = expiryDate?.let { ChronoUnit.DAYS.between(today, it) }

                daysLeft != null && daysLeft in 0..3
            }

            _expiredFoods.value = allItems.filter {
                val expiryDate = try {
                    it.expiryDate?.let { dateStr -> LocalDate.parse(dateStr) }
                } catch (e: Exception) {
                    null
                }

                expiryDate?.isBefore(today) == true
            }
        }
    }

    fun addFood(item: FoodItemEntity) {
        viewModelScope.launch {
            Log.d("FoodViewModel", "准备插入食材，当前 userId = $userId")
            dao.insertFoodItem(item)
            loadFoodItems()
        }
    }

    fun deleteFood(item: FoodItemEntity) {
        viewModelScope.launch {
            dao.deleteFoodItem(item)
            loadFoodItems()
        }
    }
}
