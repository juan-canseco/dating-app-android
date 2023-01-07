package com.org.datingapp.features.onboarding.interests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.databinding.InterestRowBinding

class InterestAdapter(private val interests : List<InterestListItemView>,
                      private val onInterestChecked : (interest : Interest) -> Unit,
                      private val onInterestUnchecked : (interest : Interest) -> Unit)
    : RecyclerView.Adapter<InterestAdapter.InterestViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val binding = InterestRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InterestViewHolder(binding, onInterestChecked, onInterestUnchecked)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val interest = interests[position]
        holder.bind(interest)
    }

    override fun getItemCount() = interests.size

    private fun unselectAll() =  interests.forEach { it.isSelected.set(false) }

    fun selectInterests(selectedInterests : HashSet<Interest>) {
        unselectAll()
        selectedInterests.forEach { selectedInterest ->
            val interest = this.interests.find { it.id == selectedInterest.id }
            interest?.isSelected?.set(true)
        }
    }

    class InterestViewHolder(private val binding : InterestRowBinding,
                             private val onInterestChecked: (interest: Interest) -> Unit,
                             private val onInterestUnchecked: (interest: Interest) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItemVm : InterestListItemView) {
            binding.vm = listItemVm
            binding.root.setOnClickListener {

                val value = listItemVm.isSelected.get() != true
                listItemVm.isSelected.set(value)

                val interest = Interest(
                    listItemVm.id,
                    listItemVm.description)

                if (value)
                    onInterestChecked.invoke(interest)
                else
                    onInterestUnchecked.invoke(interest)
            }
        }
    }
}