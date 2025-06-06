package com.zjgsu.cook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zjgsu.cook.FoodViewModel  // 替换为你的 FoodViewModel 实际路径

class FoodViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
