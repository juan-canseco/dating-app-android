package com.org.datingapp.features.onboarding.username

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.org.datingapp.R
import com.org.datingapp.databinding.FragmentUsernameBinding
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.StepsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsernameFragment : Fragment() {

    private var _binding : FragmentUsernameBinding? = null
    private val binding get() = _binding!!
    private val usernameViewModel by viewModels<UsernameViewModel>()
    private val usernamePreferencesViewModel by viewModels<UsernamePreferencesViewModel>()
    private val stepsViewModel by activityViewModels<StepsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.setStep(StepsViewModel.UsernameStep)
        binding.model = usernameViewModel
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            val isValid = usernameViewModel.validate()
            if (isValid) {
                usernamePreferencesViewModel.storeUsername(usernameViewModel.username)
            }
        }
        usernameViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        usernamePreferencesViewModel.events.observe(viewLifecycleOwner, this::handlePreferencesEvents)
        usernamePreferencesViewModel.getUsername()
    }

    private fun handlePreferencesEvents(event : Event<UsernamePreferencesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is UsernamePreferencesViewModel.Navigation.ShowGetUsername -> {
                    usernameViewModel.username = navigation.username
                }
                UsernamePreferencesViewModel.Navigation.ShowStoreUsername -> {
                    val action = UsernameFragmentDirections.actionUsernameFragmentToDescriptionFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun handleEvents(event : Event<UsernameViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is UsernameViewModel.Navigation.ShowValidUsername -> {
                    binding.username.endIconDrawable = requireActivity().getDrawable(R.drawable.ic_baseline_check_24)
                }
                is UsernameViewModel.Navigation.ShowInvalidUsername -> {
                    binding.username.endIconDrawable = null
                }
                is UsernameViewModel.Navigation.ShowUsernameError -> {
                    binding.username.error = getString(R.string.failure_on_boarding_invalid_username)
                }
                is UsernameViewModel.Navigation.ShowClearUsernameError -> {
                    binding.username.error = null
                }
                is UsernameViewModel.Navigation.ShowUsernameRequiredError -> {
                    binding.username.error = getString(R.string.failure_on_boarding_username_required)
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
        fun newInstance() = UsernameFragment()
    }
}