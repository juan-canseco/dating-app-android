package com.org.datingapp.features.home.explore.details

import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.org.datingapp.R
import com.org.datingapp.core.extension.addChip
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentUserDetailsLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class UserProfileDetailsDialogFragment : DialogFragment() {

    private var _binding : FragmentUserDetailsLayoutBinding? = null
    private val binding get() = _binding!!

    private val args : UserProfileDetailsDialogFragmentArgs by navArgs()

    private val getUserProfileDetailsViewModel by viewModels<GetUserProfileDetailsViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailsLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingViewModel.isLoading = true
        binding.loadingViewModel = loadingViewModel
        binding.retryButton.setOnClickListener {
            loadingViewModel.isLoading = true
            binding.retryButton.visibility = View.GONE
            binding.errorLabel.visibility = View.GONE
        }
        getUserProfileDetailsViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        getUserProfileDetailsViewModel.getDetails(args.userProfileId)
    }

    private fun handleEvents(event : Event<GetUserProfileDetailsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                GetUserProfileDetailsViewModel.Navigation.ShowUserDetailsStart -> {
                    loadingViewModel.isLoading = true
                }
                is GetUserProfileDetailsViewModel.Navigation.ShowUserDetailsSuccess -> {
                    loadingViewModel.isLoading = false
                    val userProfileView = navigation.userProfileView
                    binding.name.text = userProfileView.fullNameWithAge

                    binding.commonPercentage.text = "${userProfileView.matchPercentage}% Match!"
                    binding.commonPercentage.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), userProfileView.interestsColor))



                    binding.description.text = userProfileView.description


                    binding.username.text = userProfileView.username

                    binding.gender.text = userProfileView.gender

                    binding.orientation.text = userProfileView.orientation


                    val carouselItems = userProfileView.profilePicturesUris.map { CarouselItem(imageUrl = it) }
                    binding.profilePictureCarousel.setData(carouselItems)


                    userProfileView.interests.forEach {
                        binding.interestsGroup.addChip(
                            it.name,
                            requireContext(),
                            userProfileView.interestsColor,
                            1.0f)
                    }

                    userProfileView.activities.forEach {
                        binding.activitiesGroup.addChip(
                            it.name,
                            requireContext(),
                            userProfileView.interestsColor,
                            1.0f)
                    }
                }
                GetUserProfileDetailsViewModel.Navigation.ShowUserDetailsUserNotFoundError -> {
                    binding.loadingBar.visibility = View.GONE
                    binding.retryButton.visibility = View.VISIBLE
                    binding.errorLabel.visibility = View.VISIBLE
                }
                GetUserProfileDetailsViewModel.Navigation.ShowUserDetailsError -> {
                    binding.loadingBar.visibility = View.GONE
                    binding.retryButton.visibility = View.VISIBLE
                    binding.errorLabel.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun whatsAppInstalled(url : String) : Boolean {
        return try {
            val packageManager : PackageManager? = requireActivity().packageManager
            packageManager?.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        }
        catch (e : Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}