package com.org.datingapp.features.home.profile.edit.orientations

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.org.datingapp.R
import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentEditOrientationsBinding
import com.org.datingapp.features.onboarding.orientation.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditOrientationsFragment : Fragment() {

    private var _binding : FragmentEditOrientationsBinding? = null
    private val binding get() = _binding!!

    private val orientationViewModel by viewModels<OrientationViewModel>()
    private val getOrientationsViewModel by viewModels<GetOrientationsViewModel>()
    private val editOrientationsViewModel by viewModels<EditOrientationsViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()

    private lateinit var adapter : OrientationAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditOrientationsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.editButton.setOnClickListener {

            if (loadingViewModel.isLoading)
                return@setOnClickListener

            if (orientationViewModel.isValid) {
                editOrientationsViewModel.edit(orientationViewModel.orientations)
            }
        }

        orientationViewModel.events.observe(viewLifecycleOwner, this::handelEvents)
        getOrientationsViewModel.events.observe(viewLifecycleOwner, this::handleGetEvents)
        editOrientationsViewModel.events.observe(viewLifecycleOwner, this::handleEditEvents)
        orientationViewModel.getOrientations()

    }

    private fun setOrientationsLists(orientations : List<OrientationListItemViewModel>) {
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        adapter = OrientationAdapter(
            orientations,
            {orientation -> onOrientationChecked(orientation)} ,
            {orientation -> onOrientationUnchecked(orientation)})

        binding.orientationRecycler.adapter = adapter
        binding.orientationRecycler.layoutManager = layoutManager

        val dividerItemDecorator = DividerItemDecoration(
            binding.orientationRecycler.context,
            layoutManager.orientation
        )
        binding.orientationRecycler.addItemDecoration(dividerItemDecorator)
    }

    private fun selectOrientations(selectedOrientations: HashSet<Orientation>) {
        adapter.selectOrientations(selectedOrientations)
    }

    private fun onOrientationChecked(orientation : Orientation) {

        if (orientationViewModel.count == Constants.MaxNumberOfOrientations)
            return

        orientationViewModel.addOrientation(orientation)
        changeButtonState(orientationViewModel.count, orientationViewModel.isValid)
    }

    private fun onOrientationUnchecked(orientation : Orientation) {
        orientationViewModel.removeOrientation(orientation)
        changeButtonState(orientationViewModel.count, orientationViewModel.isValid)
    }


    private fun handelEvents(event : Event<OrientationViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is OrientationViewModel.Navigation.ShowGetAll -> {
                    setOrientationsLists(navigation.orientations)
                    getOrientationsViewModel.getOrientations()
                }
            }
        }
    }


    private fun handleGetEvents(event : Event<GetOrientationsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GetOrientationsViewModel.Navigation.ShowGetOrientations -> {
                    orientationViewModel.orientations = navigation.orientations
                    selectOrientations(navigation.orientations)
                    changeButtonState(orientationViewModel.count, orientationViewModel.isValid)
                }
            }
        }
    }

    private fun handleEditEvents(event : Event<EditOrientationsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                EditOrientationsViewModel.Navigation.ShowEditOrientationsStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.editButton)
                }
                EditOrientationsViewModel.Navigation.ShowEditOrientationsSuccess -> {
                    requireActivity().onBackPressed()
                }
                EditOrientationsViewModel.Navigation.ShowEditOrientationsError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.editButton)
                    Toast.makeText(requireContext(), R.string.failure_server_error_retry, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun changeButtonState(count : Int, isModelValid : Boolean) {
        if (!isModelValid)
        {
            binding.editButton.isEnabled = false
            binding.editButton.text =  getString(R.string.action_select)
        }
        else {
            binding.editButton.isEnabled = true
            binding.editButton.text = "${getString(R.string.action_select)} $count"
        }
    }

    private fun showProgressBarCenter(button : Button) {
        button.showProgress {
            progressColor = Color.WHITE
            gravity = DrawableButton.GRAVITY_CENTER
        }
    }

    private fun disableProgressBarCenter(button : Button) {
        button.hideProgress(R.string.action_edit)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditOrientationsFragment()
    }
}