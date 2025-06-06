package com.zjgsu.cook
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zjgsu.cook.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity(){

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.layoutTheme.setOnClickListener {
            startActivity(Intent(this, ThemeSettingActivity::class.java))
        }

        binding.layoutLanguage.setOnClickListener {
            startActivity(Intent(this, LanguageSettingActivity::class.java))
        }
        binding.layoutExit.setOnClickListener{
            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            prefs.edit().putBoolean("is_logged_in", false).apply()

            // 弹出提示框，告知用户已退出登录（可选）
            Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show()

            // 跳转到登录页面
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // 结束当前活动
            finish()
        }
    }
}
