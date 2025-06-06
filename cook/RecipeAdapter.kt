package com.zjgsu.cook

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zjgsu.cook.databinding.ItemRecipeBinding

class RecipeAdapter(private val context: Context, private val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)

    inner class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageFavorite.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val recipe = recipes[position]
                    val isFavorited = sharedPrefs.getBoolean(recipe.title, recipe.isFavorite)
                    val editor = sharedPrefs.edit()

                    // Toggle favorite status
                    if (isFavorited) {
                        editor.remove(recipe.title)
                        Toast.makeText(context, "${recipe.title} 已取消收藏", Toast.LENGTH_SHORT).show()
                    } else {
                        editor.putBoolean(recipe.title, true)
                        Toast.makeText(context, "${recipe.title} 已加入收藏", Toast.LENGTH_SHORT).show()
                    }
                    editor.apply()

                    // Update the icon
                    updateFavoriteIcon(binding.imageFavorite, !isFavorited)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecipeViewHolder(binding)
    }



    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.binding.apply {
            textTitle.text = recipe.title
            textDescription.text = recipe.description
            imageRecipe.setImageResource(recipe.imageResId)

            // Set favorite icon based on sharedPrefs
            val isFavorited = sharedPrefs.getBoolean(recipe.title, false)
            updateFavoriteIcon(imageFavorite, isFavorited)
            root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, RecipeDetailActivity::class.java).apply {
                    putExtra("recipe", recipe)
                }
                context.startActivity(intent)

            }
        }
    }

    fun updateData(newRecipes: List<Recipe>) {
        (recipes as? MutableList<Recipe>)?.apply {
            clear()
            addAll(newRecipes)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = recipes.size

    private fun updateFavoriteIcon(favoriteIcon: ImageView, isFavorited: Boolean) {
        if (isFavorited) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite)
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}
