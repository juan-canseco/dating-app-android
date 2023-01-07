package com.org.datingapp.features.home.profile.edit

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.org.datingapp.databinding.PhotoDetailsEmptyRowBinding
import com.org.datingapp.databinding.PhotoDetailsRowBinding
import com.org.datingapp.features.onboarding.photos.Constants.Companion.MaxNumberOfPhotos

class EditProfileDetailsPhotosAdapter
constructor(private val click : () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var profilePicturesUris : MutableList<Uri> = MutableList(MaxNumberOfPhotos){  Uri.EMPTY }


    fun refreshPhotos(picturesUris : List<Uri>) {
        for (i in 0 until MaxNumberOfPhotos) {
            profilePicturesUris[i] = Uri.EMPTY
        }
        picturesUris.forEachIndexed { index,  uri ->
            profilePicturesUris[index] = uri
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EMPTY_PHOTO_VIEW) {
            val binding = PhotoDetailsEmptyRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
            EmptyPhotoViewHolder(binding, click)
        }
        else {
            val binding = PhotoDetailsRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
            LoadedPhotoViewHolder(binding, click)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            EMPTY_PHOTO_VIEW -> {
                (holder as EmptyPhotoViewHolder).bind()
            }
            LOADED_PHOTO_VIEW -> {
                (holder as LoadedPhotoViewHolder).bind(profilePicturesUris[position])
            }
        }
    }

    override fun getItemCount() = profilePicturesUris.size

    override fun getItemViewType(position: Int): Int {
        val photoUri = profilePicturesUris[position]
        return if (photoUri == Uri.EMPTY){
            EMPTY_PHOTO_VIEW
        }
        else {
            LOADED_PHOTO_VIEW
        }
    }


    inner class EmptyPhotoViewHolder(private val binding : PhotoDetailsEmptyRowBinding,
                                     private val click : () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                click()
            }
        }
    }

    inner class LoadedPhotoViewHolder(private val binding : PhotoDetailsRowBinding,
                                      private val click : () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri : Uri) {
            Glide
                .with(binding.root.context)
                .load(uri)
                .centerCrop()
                .into(binding.photo)

            binding.photo.setImageURI(uri)
            binding.photo.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.root.setOnClickListener {
                click()
            }
        }

    }

    companion object {
        const val EMPTY_PHOTO_VIEW = 1
        const val LOADED_PHOTO_VIEW = 2
    }
}