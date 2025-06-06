package com.zjgsu.cook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecipeDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val recipe = intent.getParcelableExtra<Recipe>("recipe")
        if (recipe != null) {
            findViewById<TextView>(R.id.textTitle).text = recipe.title
            findViewById<TextView>(R.id.textDescription).text = recipe.description
            findViewById<ImageView>(R.id.imageRecipe).setImageResource(recipe.imageResId)

            // 👇 将 steps 列表合并成一段文字
            val stepsTextView = findViewById<TextView>(R.id.textSteps)
            val stepsText = recipe.steps.joinToString(separator = "\n\n") { step ->
                "${recipe.steps.indexOf(step) + 1}. $step"
            }
            stepsTextView.text = stepsText
        }
    }
}
