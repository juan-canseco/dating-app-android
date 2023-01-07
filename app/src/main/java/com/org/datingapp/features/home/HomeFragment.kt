package com.org.datingapp.features.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.org.datingapp.R
import com.org.datingapp.databinding.FragmentHomeBinding
import com.org.datingapp.features.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {


    // check this project for reference of development of DatingApp Features
    // https://github.com/Musfick/ChatApp-MVVM-Dagger2-RxJava-Firestore

    private var _binding  : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigation()
        homeViewModel.navigationVisibility.observe(viewLifecycleOwner, this::handleNavigationVisibility)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setBottomNavigation() {
        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as? NavHostFragment
        val navController = nestedNavHostFragment?.navController
        val bottomNavigationView = binding.bottomNav
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        } else {
            throw RuntimeException("Controller not found")
        }
    }

    private fun handleNavigationVisibility(value : Boolean) {
        Log.d("TAG1", "Value is $value")
        val visibility = if (value) View.VISIBLE else View.GONE
        binding.bottomNav.visibility = visibility
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}