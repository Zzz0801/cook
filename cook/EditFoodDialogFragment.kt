package com.zjgsu.cook

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class EditFoodDialogFragment(
    private val foodName: String,
    private val quantity: String,
    private val expirationDate: String,
    private val onEditConfirmed: (String, String, String) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_edit_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etName = view.findViewById<EditText>(R.id.et_food_name)
        val etQuantity = view.findViewById<EditText>(R.id.et_food_quantity)
        val etExpiration = view.findViewById<EditText>(R.id.et_food_expiration)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm_edit)

        etName.setText(foodName)
        etQuantity.setText(quantity)
        etExpiration.setText(expirationDate)

        // 日期选择器
        etExpiration.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(requireContext(), { _, y, m, d ->
                val selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                etExpiration.setText(selectedDate)
            }, year, month, day)
            dialog.show()
        }

        // 点击确认修改
        btnConfirm.setOnClickListener {
            val newName = etName.text.toString()
            val newQty = etQuantity.text.toString()
            val newDate = etExpiration.text.toString()
            onEditConfirmed(newName, newQty, newDate)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
