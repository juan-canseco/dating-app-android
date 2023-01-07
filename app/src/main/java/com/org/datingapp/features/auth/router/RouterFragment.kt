package com.org.datingapp.features.auth.router

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentRouterBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RouterFragment : Fragment() {

    private val routerViewModel by viewModels<RouterViewModel>()

    private var _binding : FragmentRouterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRouterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        routerViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        routerViewModel.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleEvents(event  : Event<RouterViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is RouterViewModel.Navigation.ShowUserLoggedIn -> {
                    val action = RouterFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
                is RouterViewModel.Navigation.ShowUserProfileIncomplete -> {
                    val action = RouterFragmentDirections.actionSplashScreenFragmentToOnBoardingFragment()
                    findNavController().navigate(action)
                }
                is RouterViewModel.Navigation.ShowUserNotLoggedIn -> {
                    val action = RouterFragmentDirections.actionSplashScreenFragmentToAuthFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RouterFragment()
    }
}