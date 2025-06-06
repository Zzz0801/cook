package com.zjgsu.cook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zjgsu.cook.R
import com.zjgsu.cook.databinding.ActivityLanguageSettingBinding

class LanguageSettingActivity : BaseActivity(){

    private lateinit var binding: ActivityLanguageSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChangeLanguage.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("中文", "English")
        MaterialAlertDialogBuilder(this)
            .setTitle("选择语言")
            .setItems(languages) { _, which ->
                when (which) {
                    0 -> LanguageHelper.setLocale(this, "zh")
                    1 -> LanguageHelper.setLocale(this, "en")
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
            .show()
    }
}
