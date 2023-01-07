package com.org.datingapp.features.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private var _binding : FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private val stepsViewModel by activityViewModels<StepsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleEvents(event : Event<StepsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is StepsViewModel.Navigation.ShowProgressChange -> {
                    binding.stepsBar.progress = navigation.progress
                }
                StepsViewModel.Navigation.ShowHideProgress -> {
                    binding.stepsBar.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = OnBoardingFragment()
    }
}