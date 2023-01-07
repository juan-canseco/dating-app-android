package com.org.datingapp.features.home.profile.preferences

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.org.datingapp.databinding.FragmentPreferencesBinding
import com.org.datingapp.features.HomeViewModel

class PreferencesFragment : Fragment() {

    private var _binding : FragmentPreferencesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreferencesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                activity?.onBackPressed()
                homeViewModel.changeNavigationVisibility(true)
            }
        })
        binding.topAppBar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        homeViewModel.changeNavigationVisibility(false)
        binding.accountSettings.setOnClickListener {
            val action = PreferencesFragmentDirections.actionPreferencesFragmentToAccountPreferencesFragment()
            findNavController().navigate(action)
        }
        binding.blockedUserSettings.setOnClickListener {

        }
        binding.privacyPolicySettings.setOnClickListener {

        }
        binding.serviceTermsSettings.setOnClickListener {

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PreferencesFragment()
    }
}