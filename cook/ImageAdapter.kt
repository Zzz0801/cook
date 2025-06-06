package com.zjgsu.cook

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter

class ImageAdapter(private val images: List<Int>) : BannerAdapter<Int, ImageAdapter.ImageHolder>(images) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ImageHolder(imageView)
    }

    override fun onBindView(holder: ImageHolder, data: Int, position: Int, size: Int) {
        holder.imageView.setImageResource(data)
    }

    inner class ImageHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}
