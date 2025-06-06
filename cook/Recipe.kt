package com.zjgsu.cook

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(
    val title: String,
    val description: String,
    val imageResId: Int,
    var isFavorite: Boolean = false,
    val steps: List<String>  // 添加图文步骤
) : Parcelable

@Parcelize
data class Step(
    val text: String,
    val imageResId: Int
) : Parcelable
