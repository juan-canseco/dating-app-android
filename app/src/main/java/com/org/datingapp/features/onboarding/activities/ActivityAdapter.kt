package com.org.datingapp.features.onboarding.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.databinding.ActivityRowBinding

class ActivityAdapter(private val activities : List<ActivityListItemView>,
                      private val onActivityChecked : (activity : Activity) -> Unit,
                      private val onActivityUnchecked : (activity : Activity) -> Unit)
    : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ActivityRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding, onActivityChecked, onActivityUnchecked)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.bind(activity)
    }

    override fun getItemCount() = activities.size

    private fun unselectAll() =  activities.forEach { it.isSelected.set(false) }

    fun selectActivities(selectedActivities : HashSet<Activity>) {
        unselectAll()
        selectedActivities.forEach { selectedActivity ->
            val activity = this.activities.find { it.id == selectedActivity.id }
            activity?.isSelected?.set(true)
        }
    }

    class ActivityViewHolder(private val binding : ActivityRowBinding,
                             private val onActivityChecked: (activity: Activity) -> Unit,
                             private val onActivityUnchecked: (activity: Activity) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listItemVm : ActivityListItemView) {
            binding.vm = listItemVm
            binding.root.setOnClickListener {

                val value = listItemVm.isSelected.get() != true
                listItemVm.isSelected.set(value)

                val activity = Activity(
                    listItemVm.id,
                    listItemVm.description)

                if (value)
                    onActivityChecked.invoke(activity)
                else
                    onActivityUnchecked.invoke(activity)
            }
        }
    }
}