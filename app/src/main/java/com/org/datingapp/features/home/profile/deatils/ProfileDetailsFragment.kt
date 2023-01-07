package com.org.datingapp.features.home.profile.deatils

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.org.datingapp.R
import com.org.datingapp.core.extension.addChip
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentProfileDetailsBinding
import com.org.datingapp.features.HomeViewModel
import com.org.datingapp.features.onboarding.birthdate.AgeCalculator
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment() {

    private var _binding : FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val getProfileDetailsViewModel by viewModels<GetProfileDetailsViewModel>()

    @Inject
    lateinit var ageCalculator: AgeCalculator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileDetailsBinding.inflate(layoutInflater, container, false)
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
        homeViewModel.changeNavigationVisibility(false)

        binding.profilePictureCarousel.registerLifecycle(viewLifecycleOwner)
        getProfileDetailsViewModel.getProfile()
        getProfileDetailsViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
    }

    private fun handleEvents(event : Event<GetProfileDetailsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                GetProfileDetailsViewModel.Navigation.ShowGetProfileDetailsStart -> {

                }
                is GetProfileDetailsViewModel.Navigation.ShowGetProfileDetailsSuccess -> {

                    val profileDetails = navigation.profileDetails
                    binding.name.text = profileDetails.fullNameWithAge

                    binding.description.text = profileDetails.description

                    val carouselItems = profileDetails.profilePicturesUris.map { CarouselItem(imageUrl = it) }
                    binding.profilePictureCarousel.setData(carouselItems)


                    binding.username.text = profileDetails.username

                    binding.gender.text = profileDetails.gender

                    binding.orientation.text = profileDetails.orientation

                    profileDetails.interests.forEach {
                        binding.interestsGroup.addChip(
                            it.name,
                            requireContext(),
                            R.color.colorPrimaryDark,
                            1.0f)
                    }

                    profileDetails.activities.forEach {
                        binding.activitiesGroup.addChip(
                            it.name,
                            requireContext(),
                            R.color.colorPrimaryDark,
                            1.0f)
                    }
                }
                GetProfileDetailsViewModel.Navigation.ShowServerError -> {
                    Log.d("TAG1", "Error")
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
        fun newInstance() = ProfileDetailsFragment()
    }
}