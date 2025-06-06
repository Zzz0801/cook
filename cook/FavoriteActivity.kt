package com.zjgsu.cook

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zjgsu.cook.Recipe
import com.zjgsu.cook.databinding.ActivityFavoriteBinding

class FavoriteActivity: BaseActivity(){

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: RecipeAdapter

    private val favoriteRecipes = mutableListOf<Recipe>()
    private val sharedPrefs: SharedPreferences by lazy {
        getSharedPreferences("Favorites", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadFavoriteRecipes()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = RecipeAdapter(this, favoriteRecipes)
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorites.adapter = favoriteAdapter
    }

    private fun loadStepsFromRaw(@RawRes resId: Int): List<String> {
        val inputStream = resources.openRawResource(resId)
        return inputStream.bufferedReader().readLines()
    }


    private fun loadFavoriteRecipes() {
        // 从 SharedPreferences 中加载已收藏的菜谱
        val allEntries = sharedPrefs.all
        for ((key, value) in allEntries) {
            if (value == true) { // 如果是收藏状态
                val recipe = getRecipeByTitle(key)
                recipe?.let { favoriteRecipes.add(it) }
            }
        }
        favoriteAdapter.notifyDataSetChanged()
    }

    // 根据标题查找对应的菜谱
    private fun getRecipeByTitle(title: String): Recipe? {
        // 这里假设你有一个方法来通过标题找到对应的菜谱数据
        val allRecipes = listOf(
            Recipe("汉堡", "西餐", R.drawable.hamburger, steps = loadStepsFromRaw(R.raw.hamburger_steps)),
            Recipe("饺子", "中餐", R.drawable.dumplings, steps = loadStepsFromRaw(R.raw.dumplings_steps)),
            Recipe("意大利面", "西餐", R.drawable.noodle, steps = loadStepsFromRaw(R.raw.noodle)),
            Recipe("三明治", "西餐", R.drawable.sandwich, steps = loadStepsFromRaw(R.raw.sandwich)),
            Recipe("罗宋汤", "韩料", R.drawable.soup, steps = loadStepsFromRaw(R.raw.soup_steps))
        )

        return allRecipes.find { it.title == title }
    }
}
