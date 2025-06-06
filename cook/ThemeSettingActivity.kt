package com.zjgsu.cook
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zjgsu.cook.R
import com.zjgsu.cook.databinding.ActivityThemeSettingBinding

class ThemeSettingActivity : BaseActivity() {

    private lateinit var binding: ActivityThemeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChangeTheme.setOnClickListener {
            showThemeDialog()
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf("浅色模式", "深色模式", "系统默认")
        MaterialAlertDialogBuilder(this)
            .setTitle("选择主题")
            .setItems(themes) { _, which ->
                when (which) {
                    0 -> AppThemeHelper.setLightMode(this)
                    1 -> AppThemeHelper.setDarkMode(this)
                    2 -> AppThemeHelper.setSystemDefault(this)
                }
            }
            .show()
    }
}
