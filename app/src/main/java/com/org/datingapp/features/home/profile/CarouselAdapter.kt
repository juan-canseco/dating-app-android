package com.org.datingapp.features.home.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.org.datingapp.databinding.ProfileImageItemBinding

class  CarouselAdapter(private val photosUris : List<String>) :
    RecyclerView.Adapter<CarouselAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ProfileImageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = photosUris[position]
        holder.bind(uri)
    }

    override fun getItemCount(): Int = photosUris.size

    inner class ImageViewHolder(val binding : ProfileImageItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoUri : String) {

        }
    }
}