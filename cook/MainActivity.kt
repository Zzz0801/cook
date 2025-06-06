package com.zjgsu.cook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zjgsu.cook.RecipeListFragment
import com.zjgsu.cook.ProfileFragment
import com.zjgsu.cook.SearchFragment

class MainActivity : BaseActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)


        bottomNav = findViewById(R.id.bottom_navigation)

        // 默认加载首页
        loadFragment(RecipeListFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    loadFragment(RecipeListFragment())
                    true
                }
                R.id.menu_ingredients -> {
                    loadFragment(FoodManagementFragment())
                    true
                }

                R.id.menu_search -> {
                    loadFragment(SearchFragment())
                    true
                }
                R.id.menu_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
