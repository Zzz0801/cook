package com.zjgsu.cook

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.zjgsu.cook.data.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity: BaseActivity() {

    private lateinit var imageAvatar: ImageView
    private lateinit var editUsername: EditText
    private lateinit var btnSave: Button
    private lateinit var userDao: UserDao
    private var userId: Int = 0

    // 图片选择器，注册回调
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                // 保存图片到内部存储
                val localPath = saveImageToInternalStorage(this@EditProfileActivity, it)
                if (localPath != null) {

                    // ✅ 只更新数据库
                    userDao.updateAvatar(userId, localPath)

                    withContext(Dispatchers.Main) {
                        Glide.with(this@EditProfileActivity)
                            .load(File(localPath))
                            .into(imageAvatar)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditProfileActivity, "头像保存失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        imageAvatar = findViewById(R.id.imageAvatar)
        editUsername = findViewById(R.id.editUsername)
        btnSave = findViewById(R.id.btnSave)

        lifecycleScope.launch(Dispatchers.IO) {
            val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            userId = prefs.getInt("user_id", 0)
            val user = userDao.getUserById(userId)
            Log.d("Debug", "EditProfileActivity 中读取的 userId = $userId")
            withContext(Dispatchers.Main) {
                if (user != null) {
                    editUsername.setText(user.username)
                    // 头像加载等
                } else {
                    // 处理用户不存在情况，比如显示默认信息或提示
                    editUsername.setText("")
                    imageAvatar.setImageResource(R.drawable.ic_person)
                }
            }


        }


        userDao = (application as MyApplication).database.userDao()

        imageAvatar.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            val newUsername = editUsername.text.toString().trim()


            if (newUsername.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    userDao.updateUsername(userId, newUsername)
                    Log.d("Debug", "EditProfileActivity 中读取的 newUsername = $newUsername")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditProfileActivity, "信息已保存", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this@EditProfileActivity, "用户名不能为空", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // 将选择的图片保存到私有目录并返回路径
    private fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "avatar_${System.currentTimeMillis()}.jpg"
            Log.d("Debug", "EditProfileActivity 中读取的 fileName = $fileName")
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
