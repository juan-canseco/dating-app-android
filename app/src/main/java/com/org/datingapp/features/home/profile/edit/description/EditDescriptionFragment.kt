package com.org.datingapp.features.home.profile.edit.description

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.org.datingapp.R
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentEditDescriptionBinding
import com.org.datingapp.features.onboarding.description.DescriptionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditDescriptionFragment : Fragment() {

    private var _binding : FragmentEditDescriptionBinding? = null
    private val binding get() = _binding!!
    private val getDescriptionViewModel by viewModels<GetDescriptionViewModel>()
    private val descriptionViewModel by viewModels<DescriptionViewModel>()
    private val editDescriptionViewModel by viewModels<EditDescriptionViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditDescriptionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.model = descriptionViewModel
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            if (loadingViewModel.isLoading) {
                requireActivity().onBackPressed()
            }
            if (!descriptionViewModel.validate()) {
                return@setOnClickListener
            }
            editDescriptionViewModel.edit(descriptionViewModel.description)
        }

        descriptionViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        getDescriptionViewModel.events.observe(viewLifecycleOwner, this::handleGetDescriptionEvents)
        editDescriptionViewModel.events.observe(viewLifecycleOwner, this::handleEditDescriptionEvents)
        getDescriptionViewModel.getDescription()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun handleEditDescriptionEvents(event : Event<EditDescriptionViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                EditDescriptionViewModel.Navigation.ShowEditDescriptionStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.nextButton)
                }
                EditDescriptionViewModel.Navigation.ShowEditDescriptionSuccess -> {
                    loadingViewModel.isLoading = false
                    requireActivity().onBackPressed()
                }
                EditDescriptionViewModel.Navigation.ShowEditDescriptionError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.nextButton)
                    Toast.makeText(requireContext(), R.string.failure_server_error_retry, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun handleGetDescriptionEvents(event : Event<GetDescriptionViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GetDescriptionViewModel.Navigation.ShowGetDescription -> {
                    descriptionViewModel.description = navigation.description
                }
            }
        }
    }


    private fun handleEvents(event : Event<DescriptionViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is DescriptionViewModel.Navigation.ShowRequiredDescriptionError -> {
                    binding.description.error = getString(R.string.failure_on_boarding_description_required)
                }
                is DescriptionViewModel.Navigation.ShowDescriptionError -> {
                    binding.description.error = getString(R.string.failure_on_boarding_invalid_description)
                }
                is DescriptionViewModel.Navigation.ShowDescriptionClearErrors -> {
                    binding.description.error = null
                }
                is DescriptionViewModel.Navigation.ShowValidDescription -> {
                    binding.description.endIconDrawable = requireActivity().getDrawable(R.drawable.ic_baseline_check_24)
                }
                is DescriptionViewModel.Navigation.ShowInvalidDescription -> {
                    binding.description.endIconDrawable = null
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = EditDescriptionFragment()
    }
}