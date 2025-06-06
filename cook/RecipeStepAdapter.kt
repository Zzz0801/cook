package com.zjgsu.cook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zjgsu.cook.databinding.ItemStepBinding

class RecipeStepAdapter(private val steps: List<String>) :
    RecyclerView.Adapter<RecipeStepAdapter.StepViewHolder>() {

    inner class StepViewHolder(val binding: ItemStepBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding = ItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]
        holder.binding.stepText.text = step
    }

    override fun getItemCount() = steps.size
}
