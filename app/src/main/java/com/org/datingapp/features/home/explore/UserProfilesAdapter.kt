package com.org.datingapp.features.home.explore

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.org.datingapp.databinding.ProfileCardBinding

class UserProfilesAdapter(
    private val profiles : List<UserProfileView>,
    private val click : (userProfileId : String) -> Unit) : RecyclerView.Adapter<UserProfilesAdapter.ProfileViewHolder>() {

    companion object : DiffUtil.ItemCallback<UserProfile>() {
        override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ProfileCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding, click)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.bind(profile)
    }

    inner class ProfileViewHolder(
        private val binding : ProfileCardBinding,
        private val click : (userProfileId : String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userProfileView : UserProfileView) {

            binding.fullName.text = userProfileView.fullNameWithAge
            binding.biography.text = userProfileView.description

            binding.commonPercentage.text = "${userProfileView.matchPercentage}% Match!"
            binding.commonPercentage.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, userProfileView.interestsColor))

            val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(binding.root.context)
                .load(userProfileView.profilePicturesUris[0])
                .placeholder(circularProgressDrawable)
                .into(binding.profilePicture)

            binding.profileCardLayout.setOnClickListener {
                click(userProfileView.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }
}