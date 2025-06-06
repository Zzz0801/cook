package com.zjgsu.cook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zjgsu.cook.data.AppDatabase
import com.zjgsu.cook.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : BaseActivity() {

    private lateinit var editNewEmail: EditText
    private lateinit var editNewPassword: EditText
    private lateinit var btnConfirmRegister: Button
    private lateinit var editUsername:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editNewEmail = findViewById(R.id.editNewEmail)
        editNewPassword = findViewById(R.id.editNewPassword)
        btnConfirmRegister = findViewById(R.id.btnConfirmRegister)
        editUsername = findViewById(R.id.editUsername)

        val userDao = AppDatabase.getDatabase(applicationContext).userDao()

        btnConfirmRegister.setOnClickListener {
            val newEmail = editNewEmail.text.toString().trim()
            val newPassword = editNewPassword.text.toString().trim()
            val username = editUsername.text.toString().trim()

                if (username.isNotEmpty() && newEmail.isNotEmpty() && newPassword.isNotEmpty()) {
                lifecycleScope.launch {
                    val existingUser = withContext(Dispatchers.IO) {
                        userDao.getUserByEmail(newEmail)
                    }

                    if (existingUser != null) {
                        Toast.makeText(this@RegisterActivity, "该邮箱已注册", Toast.LENGTH_SHORT).show()
                    } else {
                        val newUser = UserEntity(email = newEmail, password = newPassword, username = username, avatarPath = "D:\\cook\\app\\src\\main\\res\\drawable\\ic_person.xml")
                        withContext(Dispatchers.IO) {
                            userDao.register(newUser)
                        }
                        Toast.makeText(this@RegisterActivity, "注册成功，请登录", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
