package com.zjgsu.cook.data

import com.zjgsu.cook.FoodItem

fun FoodItem.toEntity(userId: Int): FoodItemEntity {
    return FoodItemEntity(
        name = this.name,
        quantity = this.quantity,
        expiryDate = this.expiryDate.toString(),
        userId = userId
    )
}
