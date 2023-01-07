package com.org.datingapp.features.onboarding.orientation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.databinding.OrientationRowBinding

class OrientationAdapter(private val orientations : List<OrientationListItemViewModel>,
                      private val onOrientationChecked : (orientation : Orientation) -> Unit,
                      private val onOrientationUnchecked : (orientation : Orientation) -> Unit)
    : RecyclerView.Adapter<OrientationAdapter.OrientationViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrientationViewHolder {
        val binding = OrientationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrientationViewHolder(binding,
            onOrientationChecked,
            onOrientationUnchecked
        ) { getNumberOfSelectedOrientations() }
    }

    override fun onBindViewHolder(holder: OrientationViewHolder, position: Int) {
        val orientation = orientations[position]
        holder.bind(orientation)
    }

    private fun getNumberOfSelectedOrientations() : Int {
        return orientations.count { it.checked.get() == true }
    }

    override fun getItemCount() = orientations.size

    private fun unselectAll() =  orientations.forEach { it.checked.set(false) }

    fun selectOrientations(selectedOrientations : HashSet<Orientation>) {
        unselectAll()
        selectedOrientations.forEach { selectedOrientation ->
            val orientation = this.orientations.find { it.id == selectedOrientation.id }
            orientation?.checked?.set(true)
        }
    }

    class OrientationViewHolder(private val binding : OrientationRowBinding,
                                private val onOrientationChecked: (orientation: Orientation) -> Unit,
                                private val onOrientationUnchecked: (orientation: Orientation) -> Unit,
                                private val getNumberOfSelectedOrientations : () -> Int)

        : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel : OrientationListItemViewModel) {

            binding.vm = viewModel
            binding.root.setOnClickListener {

                val value = viewModel.checked.get() != true

                if (getNumberOfSelectedOrientations() == Constants.MaxNumberOfOrientations) {
                    viewModel.checked.set(false)
                }
                else {
                    viewModel.checked.set(value)
                }

                val orientation = Orientation(
                    viewModel.id,
                    viewModel.name)

                if (value) {
                    onOrientationChecked.invoke(orientation)
                }
                else {
                    onOrientationUnchecked.invoke(orientation)
                }
            }
        }
    }
}