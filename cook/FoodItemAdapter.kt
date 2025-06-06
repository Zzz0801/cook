package com.zjgsu.cook

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zjgsu.cook.databinding.ItemFoodBinding
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class FoodItemAdapter(private val foodItems: MutableList<FoodItem>, private val onItemClick: (FoodItem) -> Unit) :
    RecyclerView.Adapter<FoodItemAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(foodItems[adapterPosition])
            }
        }
    }
    fun getItemAt(position: Int): FoodItem? {
        return if (position in 0 until foodItems.size) foodItems[position] else null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }
    var onDeleteListener: ((FoodItem) -> Unit)? = null

    fun updateData(newList: List<FoodItem>) {
        foodItems.clear()
        foodItems.addAll(newList)
        notifyDataSetChanged()
    }

    // FoodItemAdapter.kt


    fun swipeToDelete(position: Int) {
        if (position in foodItems.indices) {
            val item = foodItems[position]
            onDeleteListener?.invoke(item)  // 调用 ViewModel 的删除逻辑
        }
    }

    fun deleteItem(position: Int) {
        if (position in 0 until foodItems.size) {
            val item = foodItems[position]
            Log.d("Adapter", "Removing item from list: $item")
            foodItems.removeAt(position)
            notifyItemRemoved(position)
            onDeleteListener?.invoke(item)
        } else {
            Log.w("Adapter", "Tried to delete invalid position: $position, list size: ${foodItems.size}")
        }
    }



    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodItems[position]
        val today = LocalDate.now()
        val expiryDateStr = foodItem.expiryDate?.toString()

        holder.binding.apply {
            textFoodName.text = foodItem.name
            textQuantity.text = "数量：${foodItem.quantity}"

            if (expiryDateStr != null) {
                try {
                    val expiryDate = LocalDate.parse(expiryDateStr)
                    val daysLeft = ChronoUnit.DAYS.between(today, expiryDate).toInt()

                    textExpirationDate.text = when {
                        daysLeft > 0 -> "还有 $daysLeft 天过期"
                        daysLeft == 0 -> "今天过期"
                        else -> "已过期 ${-daysLeft} 天"
                    }
                } catch (e: Exception) {
                    textExpirationDate.text = "过期时间格式错误"
                }
            } else {
                textExpirationDate.text = "过期时间未知"
            }
        }
    }


    override fun getItemCount() = foodItems.size
}
