package com.zjgsu.cook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zjgsu.cook.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var allRecipes: List<Recipe>  // 延后初始化

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化 allRecipes（此时 Fragment 已附加到 Context，可以安全访问资源）
        allRecipes = listOf(
            Recipe("汉堡", "西餐", R.drawable.hamburger, false, steps = loadStepsFromRaw(R.raw.hamburger_steps)),
            Recipe("饺子", "中餐", R.drawable.dumplings, false, steps = loadStepsFromRaw(R.raw.dumplings_steps)),
            Recipe("意大利面", "西餐", R.drawable.noodle, false, steps = loadStepsFromRaw(R.raw.noodle)),
            Recipe("三明治", "西餐", R.drawable.sandwich, false, steps = loadStepsFromRaw(R.raw.sandwich)),
            Recipe("罗宋汤", "韩料", R.drawable.soup, false, steps = loadStepsFromRaw(R.raw.soup_steps))
        )

        // 随机推荐 3 个菜谱
        val recommendedRecipes = allRecipes.shuffled().take(3)

        // 设置 RecyclerView
        recipeAdapter = RecipeAdapter(requireContext(), recommendedRecipes)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }

        binding.btnRefresh.setOnClickListener {
            recipeAdapter.updateData(getRandomRecipes())
        }

        setupBanner()
    }

    private fun getRandomRecipes(): List<Recipe> {
        return allRecipes.shuffled().take(3)
    }

    private fun loadStepsFromRaw(@RawRes resId: Int): List<String> {
        val inputStream = resources.openRawResource(resId)
        return inputStream.bufferedReader().readLines()
    }

    private fun setupBanner() {
        val images = listOf(
            R.drawable.noodle,
            R.drawable.sandwich,
            R.drawable.dumplings,
            R.drawable.hamburger,
            R.drawable.soup
        )
        binding.banner.setAdapter(ImageAdapter(images))
            .isAutoLoop(true)
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
