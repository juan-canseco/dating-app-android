package com.org.datingapp.features.onboarding.description

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.org.datingapp.R
import com.org.datingapp.databinding.FragmentDescriptionBinding
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.StepsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DescriptionFragment : Fragment() {

    private var _binding : FragmentDescriptionBinding? = null
    private val binding get() = _binding!!
    private val descriptionViewModel by viewModels<DescriptionViewModel>()
    private val descriptionPreferencesViewModel by viewModels<DescriptionPreferencesViewModel>()
    private val stepsViewModel by activityViewModels<StepsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.setStep(StepsViewModel.DescriptionStep)
        binding.model = descriptionViewModel
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            val isValid = descriptionViewModel.validate()
            if (isValid) {
                descriptionPreferencesViewModel.storeDescription(descriptionViewModel.description)
            }
        }
        binding.omitButton.setOnClickListener {
            val action = DescriptionFragmentDirections.actionDescriptionFragmentToInterestsFragment()
            findNavController().navigate(action)
        }
        descriptionViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        descriptionPreferencesViewModel.events.observe(viewLifecycleOwner, this::handlePreferenceEvents)
        descriptionPreferencesViewModel.getDescription()
    }

    private fun handlePreferenceEvents(event : Event<DescriptionPreferencesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is DescriptionPreferencesViewModel.Navigation.ShowGetDescription -> {
                    descriptionViewModel.description = navigation.description
                }
                DescriptionPreferencesViewModel.Navigation.ShowStoreDescription -> {
                    val action = DescriptionFragmentDirections.actionDescriptionFragmentToInterestsFragment()
                    findNavController().navigate(action)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = DescriptionFragment()
    }
}