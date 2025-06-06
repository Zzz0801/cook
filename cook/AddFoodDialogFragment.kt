package com.zjgsu.cook

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.zjgsu.cook.databinding.DialogAddFoodBinding
import com.zjgsu.cook.data.toEntity
import java.util.Calendar

class AddFoodDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAddFoodBinding
    private val foodViewModel: FoodViewModel by activityViewModels()  // 获取 Activity 中的 ViewModel

    private var listener: ((FoodItem) -> Unit)? = null


    fun setOnFoodAddedListener(listener: (FoodItem) -> Unit) {
        this.listener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etFoodAge.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val dateStr = String.format("%04d-%02d-%02d", y, m + 1, d)
                binding.etFoodAge.setText(dateStr)
            }, year, month, day)

            datePicker.show()
        }

        binding.btnAddFood.setOnClickListener {
            val name = binding.etFoodName.text.toString()
            val quantity = binding.etFoodQuantity.text.toString()
            val expirationDate = binding.etFoodAge.text.toString()

            val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getInt("user_id", -1)
            Log.d("AddFood", "当前 userId = $userId")

            if (name.isNotEmpty() && quantity.isNotEmpty() && expirationDate.isNotEmpty()) {
                if (userId != -1) {
                    val newFood = FoodItem(name, quantity, expirationDate)
                    val foodEntity = newFood.toEntity(userId)

                    foodViewModel.addFood(foodEntity)
                    Toast.makeText(context, "食材已添加", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(context, "用户未登录", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "请填写完整信息", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
