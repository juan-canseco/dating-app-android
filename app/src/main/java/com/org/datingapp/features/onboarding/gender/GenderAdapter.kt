package com.org.datingapp.features.onboarding.gender

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.databinding.GenderRowBinding

class GenderAdapter(private val genres: List<GenderListItemViewModel>,
                    private val click : (item : Gender) -> Unit)
          : RecyclerView.Adapter<GenderAdapter.GenderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {
        val binding = GenderRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return GenderViewHolder(binding, click) { unselectAll() }
    }

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        val gender = genres[position]
        holder.bind(gender)
    }

    override fun getItemCount() = genres.size

    fun select(gender : Gender) {
        unselectAll()
        val selectedGender = genres.find { g -> g.id == gender.id}
        selectedGender?.checked?.set(true)
    }

    private fun unselectAll() {
        genres.forEach { it.checked.set(false) }
    }

    class GenderViewHolder(private val binding : GenderRowBinding,
                           private val click : (item : Gender) -> Unit,
                           private val unselect : () -> Unit ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gender : GenderListItemViewModel) {
            binding.model = gender
            binding.root.setOnClickListener {
                unselect.invoke()
                val selectedGender = Gender(
                    gender.id,
                    gender.name)
                click.invoke(selectedGender)
                gender.checked.set(true)
            }
        }
    }
}