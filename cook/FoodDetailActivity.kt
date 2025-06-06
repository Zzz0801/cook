package com.zjgsu.cook

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zjgsu.cook.databinding.ActivityFoodDetailBinding

class FoodDetailActivity : BaseActivity(){

    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var foodViewModel: FoodViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取传递过来的食材对象
        val foodItem: FoodItem? = intent.getParcelableExtra("foodItem")

        foodItem?.let {
            // 设置食材的详细信息
            binding.textFoodName.text = it.name
            binding.textQuantity.text = "数量: ${it.quantity}"
            binding.textExpirationDate.text = "保质期: ${it.expiryDate}"
        }

        // 编辑食材按钮点击事件
        binding.buttonEditFood.setOnClickListener {
            // 你可以打开一个编辑页面或弹出对话框来编辑食材信息
            Toast.makeText(this, "编辑食材信息", Toast.LENGTH_SHORT).show()
        }
        binding.buttonEditFood.setOnClickListener {
            val currentName = binding.textFoodName.text.toString()
            val currentQty = binding.textQuantity.text.toString()
            val currentDate = binding.textExpirationDate.text.toString()

            val dialog = EditFoodDialogFragment(currentName, currentQty, currentDate) { newName, newQty, newDate ->

                val factory = FoodViewModelFactory(application)
                val foodViewModel = ViewModelProvider(this, factory)[FoodViewModel::class.java]



                foodViewModel.loadFoodItems()

                // UI 不需要手动更新，如果你通过 LiveData 观察数据就会自动刷新
            }
            dialog.show(supportFragmentManager, "EditFoodDialog")

        }

    }
}
