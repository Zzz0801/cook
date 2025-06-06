package com.zjgsu.cook

import com.zjgsu.cook.data.FoodItemEntity

fun FoodItemEntity.toFoodItem(): FoodItem {
    return FoodItem(
        name = this.name,
        quantity = this.quantity,
        expiryDate = this.expiryDate
    )
}