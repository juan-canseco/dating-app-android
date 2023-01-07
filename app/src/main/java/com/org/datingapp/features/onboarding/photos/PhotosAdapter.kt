package com.org.datingapp.features.onboarding.photos

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.org.datingapp.databinding.EmptyPhotoRowBinding
import com.org.datingapp.databinding.LoadedPhotoRowBinding

// https://blog.mindorks.com/recyclerview-multiple-view-types-in-android


class PhotosAdapter constructor(private val addClick : () -> Unit,
                                private val click : (position : Int, localUri : String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var photos = MutableList(Constants.MaxNumberOfPhotos) { PhotoListItemViewModel() }

    companion object {
        const val EMPTY_PHOTO_VIEW = 1
        const val LOADED_PHOTO_VIEW = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EMPTY_PHOTO_VIEW) {
            val binding = EmptyPhotoRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
            EmptyPhotoViewHolder(binding, addClick)
        } else {
            val binding = LoadedPhotoRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
            LoadedPhotoViewHolder(binding, click)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = photos[position]
        if (photo.getViewType() == EMPTY_PHOTO_VIEW)
            (holder as EmptyPhotoViewHolder).bind()
        else
            (holder as LoadedPhotoViewHolder).bind(photo, position)
    }

    override fun getItemCount(): Int = photos.size

    override fun getItemViewType(position: Int): Int {
        return photos[position].getViewType()
    }

    fun refreshPhotos(localPhotos : List<Uri>) {
        photos.forEach {
            it.uri = null
        }
        localPhotos.forEachIndexed { index, s ->
            this.photos[index].uri = s
        }
        this.notifyDataSetChanged()
    }

    inner class EmptyPhotoViewHolder(private val binding : EmptyPhotoRowBinding,
                                     private val click : () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.root.setOnClickListener {
                click()
            }
        }
    }

    inner class LoadedPhotoViewHolder(private val binding: LoadedPhotoRowBinding,
                                      private val click : (position : Int, localUri : String) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo : PhotoListItemViewModel, position : Int) {
            photo.uri?.let { uri ->
                binding.photo.setImageURI(uri)
                binding.photo.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.root.setOnClickListener {
                    click(position, uri.toString())
                }
            }
        }
    }
}