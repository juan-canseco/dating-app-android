package com.org.datingapp.features.home.profile.edit.name

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
import com.org.datingapp.databinding.FragmentEditNameBinding
import com.org.datingapp.features.onboarding.name.NameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class EditNameFragment : Fragment() {

    private var _binding : FragmentEditNameBinding? = null
    private val binding get() = _binding!!
    private val getNameViewModel by viewModels<GetNameViewModel>()
    private val nameViewModel by viewModels<NameViewModel>()
    private val editNameViewModel by viewModels<EditNameViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.model = nameViewModel
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            if (loadingViewModel.isLoading) {
                requireActivity().onBackPressed()
            }
            if (!nameViewModel.validate()) {
                return@setOnClickListener
            }
            editNameViewModel.edit(nameViewModel.fullName)
        }
        nameViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        getNameViewModel.events.observe(viewLifecycleOwner, this::handleGetNameEvents)
        editNameViewModel.events.observe(viewLifecycleOwner, this::handleEditNameEvents)
        getNameViewModel.getName()
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

    private fun handleEditNameEvents(event : Event<EditNameViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                EditNameViewModel.Navigation.ShowEditNameStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.nextButton)
                }
                EditNameViewModel.Navigation.ShowEditNameSuccess -> {
                    loadingViewModel.isLoading = false
                    requireActivity().onBackPressed()
                }
                EditNameViewModel.Navigation.ShowEditNameError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.nextButton)
                    Toast.makeText(requireContext(), R.string.failure_server_error_retry, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun handleGetNameEvents(event : Event<GetNameViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GetNameViewModel.Navigation.ShowGetName -> {
                    nameViewModel.fullName = navigation.name
                }
            }
        }
    }


    private fun handleEvents(event : Event<NameViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation->
            when (navigation) {
                is NameViewModel.Navigation.ShowFullNameClearErrors -> {
                    binding.fullName.error = null
                }
                is NameViewModel.Navigation.ShowFullNameError -> {
                    binding.fullName.error = getString(R.string.failure_on_boarding_invalid_full_name)
                }
                is NameViewModel.Navigation.ShowFullNameRequired -> {
                    binding.fullName.error = getString(R.string.failure_on_boarding_full_name_required)
                }
                is NameViewModel.Navigation.ShowFullNameValid -> {
                    binding.fullName.endIconDrawable = requireActivity().getDrawable(R.drawable.ic_baseline_check_24)
                }
                is NameViewModel.Navigation.ShowFullNameInvalid -> {
                    binding.fullName.endIconDrawable = null
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditNameFragment()
    }
}