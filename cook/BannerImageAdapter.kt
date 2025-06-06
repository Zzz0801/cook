package com.zjgsu.cook

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter

class BannerImageAdapter(private val images: List<Int>) :
    BannerAdapter<Int, BannerImageAdapter.BannerViewHolder>(images) {

    inner class BannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateHolder(parent: android.view.ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = android.view.ViewGroup.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder, data: Int, position: Int, size: Int) {
        holder.imageView.setImageResource(data)
    }
}
