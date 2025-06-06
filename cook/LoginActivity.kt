package com.zjgsu.cook
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zjgsu.cook.MainActivity
import com.zjgsu.cook.R
import com.zjgsu.cook.data.AppDatabase
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val db = AppDatabase.getDatabase(this)
                val userDao = db.userDao()

                lifecycleScope.launch {
                    val user = userDao.getUserByEmailAndPassword(email, password)
                    if (user != null) {
                        // 保存登录状态和 userId
                        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        prefs.edit()
                            .putBoolean("is_logged_in", true)
                            .putInt("user_id", user.userId)
                            .putString("username", user.username)
                            .apply()

                        Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "账号或密码错误", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show()
            }
        }



        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
