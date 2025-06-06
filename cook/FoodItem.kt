package com.zjgsu.cook

import android.os.Parcel
import android.os.Parcelable

data class FoodItem(
    val name: String,
    val quantity: String,
    val expiryDate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(quantity)
        parcel.writeString(expiryDate)
        //parcel.writeString(addedTime)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<FoodItem> {
        override fun createFromParcel(parcel: Parcel): FoodItem {
            return FoodItem(parcel)
        }

        override fun newArray(size: Int): Array<FoodItem?> {
            return arrayOfNulls(size)
        }
    }
}
