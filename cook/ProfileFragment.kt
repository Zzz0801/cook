package com.zjgsu.cook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.zjgsu.cook.data.UserDao
import com.zjgsu.cook.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import androidx.lifecycle.lifecycleScope

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userDao: UserDao
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // 获取 userId
        val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        userId = prefs.getInt("user_id", 0)
        Log.d("Debug", "当前 userId: $userId")

        userDao = (requireActivity().application as MyApplication).database.userDao()

        // 加载用户信息
        loadUserInfo()

        // 设置点击事件
        setupListeners()

        return binding.root
    }

    private fun loadUserInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = userDao.getUserById(userId)
            withContext(Dispatchers.Main) {
                // 设置用户名
                binding.textUsername.text = "Hi, ${user?.username ?: "用户"}"

                // 设置头像
                val avatarPath = user?.avatarPath
                if (!avatarPath.isNullOrEmpty()) {
                    val file = File(avatarPath)
                    if (file.exists()) {
                        Glide.with(this@ProfileFragment)
                            .load(file)
                            .skipMemoryCache(true) // 清除 Glide 内存缓存
                            .dontAnimate()         // 避免缓存动画
                            .error(R.drawable.ic_person)
                            .into(binding.imgAvatar)


                    } else {
                        binding.imgAvatar.setImageResource(R.drawable.ic_person)
                    }
                } else {
                    binding.imgAvatar.setImageResource(R.drawable.ic_person)
                }
            }
        }
    }


    private fun setupListeners() {
        binding.layoutFavorites.setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteActivity::class.java))
        }

        binding.layoutSettings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        binding.layoutProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        Glide.get(requireContext()).clearMemory()

        // 返回页面时刷新头像和用户名
        loadUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
