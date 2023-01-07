package com.org.datingapp.features.home.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.org.datingapp.R
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val getMyProfileViewModel by viewModels<GetMyProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileInfoLayout.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToProfileDetailsFragment()
            findNavController().navigate(action)
        }

        /*
        binding.topAppBar.setNavigationOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToPreferencesFragment()
            findNavController().navigate(action)
        }
         */

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.editProfile -> {
                    val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
        binding.friendsLayout.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToMyFriendsFragment()
            findNavController().navigate(action)
        }

        binding.logoutButton.setOnClickListener {
            // Sign Out
            val action = ProfileFragmentDirections.actionProfileFragmentToSignOutDialogFragment()
            findNavController().navigate(action)

        }
    }

    override fun onStart() {
        super.onStart()
        getMyProfileViewModel.getProfile()
        getMyProfileViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderUser(profileView : ProfileView) {
        val nameWithAge = "${profileView.name}, ${profileView.age}"
        binding.profileName.text = nameWithAge

        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(requireContext())
            .load(profileView.profilePictureUri)
            .placeholder(circularProgressDrawable)
            .transform(CircleCrop())
            .into(binding.profilePhoto)
    }

    private fun handleEvents(event : Event<GetMyProfileViewModel.GetMyProfileViewModelNavigation>?) {
        event?.getContentIfNotHandled()?.let {  navigation ->
            when (navigation) {
                GetMyProfileViewModel.GetMyProfileViewModelNavigation.ShowGetMyProfileStart -> {
                    Log.d("TAG1", "Start")
                }
                is GetMyProfileViewModel.GetMyProfileViewModelNavigation.ShowGetMyProfileSuccess -> {
                    renderUser(navigation.profileView)
                }
                GetMyProfileViewModel.GetMyProfileViewModelNavigation.ShowServerError -> {
                    Log.d("TAG1", "Error")
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}