package com.org.datingapp.features.onboarding.name

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.org.datingapp.R
import com.org.datingapp.databinding.FragmentNameBinding
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.StepsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameFragment : Fragment() {

    private var _binding : FragmentNameBinding? = null
    private val binding get() = _binding!!
    private val nameViewModel by viewModels<NameViewModel>()
    private val stepsViewModel by activityViewModels<StepsViewModel>()
    private val namePreferencesViewModel by viewModels<NamePreferencesViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.setStep(StepsViewModel.NameStep)
        binding.model = nameViewModel
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            val nameIsValid = nameViewModel.validate()
            if (nameIsValid) {
                namePreferencesViewModel.storeName(nameViewModel.fullName)
            }
        }
        nameViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        namePreferencesViewModel.events.observe(viewLifecycleOwner, this::handlePreferenceEvents)

        namePreferencesViewModel.getName()
    }

    private fun handlePreferenceEvents(event : Event<NamePreferencesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is NamePreferencesViewModel.Navigation.ShowGetNamePreference -> {
                    nameViewModel.fullName = navigation.name
                }
                NamePreferencesViewModel.Navigation.ShowStoreNamePreference -> {
                    val action = NameFragmentDirections.actionNameFragmentToBirthDateFragment()
                    findNavController().navigate(action)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = NameFragment()
    }
}