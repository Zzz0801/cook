package com.zjgsu.cook
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.zjgsu.cook.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter
    private val allRecipes = mutableListOf<Recipe>()
    private val filteredRecipes = mutableListOf<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupRecyclerView()
        loadAllRecipes()
        setupSearch()

        return binding.root
    }

    private fun loadStepsFromRaw(@RawRes resId: Int): List<String> {
        val inputStream = resources.openRawResource(resId)
        return inputStream.bufferedReader().readLines()
    }
    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(requireContext(),filteredRecipes)
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSearch.adapter = recipeAdapter
    }

    private fun loadAllRecipes() {
        allRecipes.add(Recipe("汉堡", "西餐", R.drawable.hamburger,steps = loadStepsFromRaw(R.raw.hamburger_steps)))
        allRecipes.add(Recipe("饺子", "中餐", R.drawable.dumplings,steps = loadStepsFromRaw(R.raw.dumplings_steps)))
        allRecipes.add(Recipe("意大利面", "西餐", R.drawable.noodle, steps = loadStepsFromRaw(R.raw.noodle)))
        allRecipes.add(Recipe("三明治", "西餐", R.drawable.sandwich, steps = loadStepsFromRaw(R.raw.sandwich)))
        allRecipes.add(Recipe("罗宋汤", "韩料", R.drawable.soup, steps = loadStepsFromRaw(R.raw.soup_steps)))
        // 先默认显示全部
        filteredRecipes.addAll(allRecipes)
        recipeAdapter.notifyDataSetChanged()
    }

    private fun setupSearch() {
        binding.editSearch.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            searchRecipes(query)
        }
    }

    private fun searchRecipes(query: String) {
        filteredRecipes.clear()
        if (query.isEmpty()) {
            filteredRecipes.addAll(allRecipes)
        } else {
            val lowerQuery = query.lowercase()
            filteredRecipes.addAll(allRecipes.filter {
                it.title.lowercase().contains(lowerQuery) || it.description.lowercase().contains(lowerQuery)
            })
        }
        recipeAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
