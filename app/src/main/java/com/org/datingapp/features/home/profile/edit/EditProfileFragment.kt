package com.org.datingapp.features.home.profile.edit

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.datingapp.core.platform.EqualSpacingItemDecoration
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentEditProfileBinding
import com.org.datingapp.features.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding : FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val getProfileEditDetailsViewModel by viewModels<GetProfileEditDetailsViewModel>()


    private lateinit var adapter : EditProfileDetailsPhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
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
        binding.photosRecycler.layoutManager =  LinearLayoutManager(requireActivity().applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.photosRecycler.addItemDecoration(EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL))
        binding.topAppBar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        homeViewModel.changeNavigationVisibility(false)

        binding.editPhotosButton.setOnClickListener {
            onPhotoClick()
        }

        binding.nameInfo.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToEditProfileNameFragment()
            findNavController().navigate(action)
        }

        binding.genderInfo.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToEditGenderFragment()
            findNavController().navigate(action)
        }

        binding.orientationInfo.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToEditOrientationsFragment()
            findNavController().navigate(action)
        }

        binding.birthdateInfo.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToEditBirthDateFragment()
            findNavController().navigate(action)
        }

        binding.descriptionInfo.setOnClickListener {
            val action  = EditProfileFragmentDirections.actionEditProfileFragmentToEditDescriptionFragment()
            findNavController().navigate(action)
        }

        binding.interestsInfo.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToEditInterestsFragment()
            findNavController().navigate(action)
        }

        binding.activitiesInfo.setOnClickListener {
            val action = EditProfileFragmentDirections.actionEditProfileFragmentToEditActivitiesFragment()
            findNavController().navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        getProfileEditDetailsViewModel.getProfileDetails()
        getProfileEditDetailsViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
    }


    private fun handleEvents(event : Event<GetProfileEditDetailsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                GetProfileEditDetailsViewModel.Navigation.ShowGetProfileEditDetailsStart -> {
                    Log.d("TAG1", "Success")
                }
                is GetProfileEditDetailsViewModel.Navigation.ShowGetProfileEditDetailsSuccess -> {
                    binding.profileEditDetailsView = navigation.profileEditDetailsView
                    setPhotos(navigation.profileEditDetailsView.profilePictures)
                }
                GetProfileEditDetailsViewModel.Navigation.ShowServerError -> {
                    Log.d("TAG1", "Success")
                }
            }
        }
    }

    private fun setPhotos(profilePictures : List<Uri>) {
        adapter = EditProfileDetailsPhotosAdapter { onPhotoClick() }
        binding.photosRecycler.adapter  = adapter
        adapter.refreshPhotos(profilePictures)
    }

    private fun onPhotoClick() {
        val action = EditProfileFragmentDirections.actionEditProfileFragmentToEditProfilePhotosFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }
}