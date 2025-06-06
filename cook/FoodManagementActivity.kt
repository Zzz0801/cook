package com.zjgsu.cook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zjgsu.cook.databinding.ActivityFoodManagementBinding

class FoodManagementActivity : BaseActivity() {

    private lateinit var binding: ActivityFoodManagementBinding
    private lateinit var foodItemAdapter: FoodItemAdapter
    private val foodItems = mutableListOf<FoodItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.buttonAddFood.setOnClickListener {
            // 打开添加食材的对话框或页面
            openAddFoodDialog()
        }
    }

    private fun setupRecyclerView() {
        foodItemAdapter = FoodItemAdapter(foodItems) { foodItem ->
            // 点击某个食材，可以实现编辑或查看食材详情等功能
            openFoodDetail(foodItem)
        }
        binding.recyclerViewFood.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFood.adapter = foodItemAdapter
    }

    private fun openAddFoodDialog() {
        // 打开对话框来添加食材
        val dialog = AddFoodDialogFragment()
        dialog.show(supportFragmentManager, "addFoodDialog")
    }
    fun addFoodItem(foodItem: FoodItem) {
        foodItems.add(foodItem)
        foodItemAdapter.notifyItemInserted(foodItems.size - 1)
    }


    private fun openFoodDetail(foodItem: FoodItem) {
        // 打开食材详情页面，或者实现编辑功能
        val intent = Intent(this, FoodDetailActivity::class.java)
        intent.putExtra("foodItem", foodItem)
        startActivity(intent)
    }
}
