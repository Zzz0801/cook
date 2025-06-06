package com.zjgsu.cook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.youth.banner.indicator.CircleIndicator
import com.zjgsu.cook.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = mutableListOf<Recipe>()

    private val bannerImages = listOf(
        R.drawable.dumplings,
        R.drawable.hamburger,
        R.drawable.chunjuan
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupBanner()
        setupRecyclerView()

        return binding.root
    }

    private fun setupBanner() {
        binding.banner.apply {
            setBannerRound(20f)
            setAdapter(BannerImageAdapter(bannerImages))
            addBannerLifecycleObserver(viewLifecycleOwner)
            indicator = CircleIndicator(context)
        }
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(requireContext(), recipeList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = recipeAdapter

        loadFakeRecipes()
    }
    private fun loadStepsFromRaw(@RawRes resId: Int): List<String> {
        val inputStream = resources.openRawResource(resId)
        return inputStream.bufferedReader().readLines()
    }
    private fun loadFakeRecipes() {
        recipeList.clear()
        recipeList.add(Recipe("饺子", "中餐", R.drawable.dumplings,steps = loadStepsFromRaw(R.raw.dumplings_steps)))
        recipeList.add(Recipe("三明治", "西餐", R.drawable.sandwich, steps = loadStepsFromRaw(R.raw.sandwich) ))
        recipeList.add(Recipe("意大利面", "西餐", R.drawable.noodle, false, steps = loadStepsFromRaw(R.raw.noodle)))
        recipeList.add(Recipe("三明治", "西餐", R.drawable.sandwich, false, steps = loadStepsFromRaw(R.raw.sandwich)) )
        recipeList.add(Recipe("罗宋汤", "韩料", R.drawable.soup, false, steps = loadStepsFromRaw(R.raw.soup_steps)))
        recipeAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
