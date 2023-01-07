package com.org.datingapp.features.onboarding.interests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.datingapp.R
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentInterestsBinding
import com.org.datingapp.features.onboarding.StepsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestsFragment : Fragment() {

    private var _binding : FragmentInterestsBinding? = null
    private val binding get() = _binding!!

    private val interestViewModel by viewModels<InterestViewModel>()
    private val interestsPreferencesViewModel by viewModels<InterestsPreferencesViewModel>()
    private val stepsViewModel by activityViewModels<StepsViewModel>()
    private lateinit var adapter : InterestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInterestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.setStep(StepsViewModel.InterestsStep)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.nextButton.setOnClickListener {
            if (interestViewModel.isValid) {
                interestsPreferencesViewModel.storeInterests(interestViewModel.interests)
            }
        }
        interestViewModel.events.observe(viewLifecycleOwner, this::handelEvents)
        interestsPreferencesViewModel.events.observe(viewLifecycleOwner, this::handlePreferenceEvents)
        interestViewModel.getInterests()
    }

    private fun setInterestsLists(interests : List<InterestListItemView>) {
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        adapter = InterestAdapter(
            interests,
            {interest -> onInterestChecked(interest)} ,
            {interest -> onInterestUnchecked(interest)})

        binding.interestsRecycler.adapter = adapter
        binding.interestsRecycler.layoutManager = layoutManager

        val dividerItemDecorator = DividerItemDecoration(
            binding.interestsRecycler.context,
            layoutManager.orientation
        )
        binding.interestsRecycler.addItemDecoration(dividerItemDecorator)
    }

    private fun selectInterests(selectedInterests: HashSet<Interest>) {
        adapter.selectInterests(selectedInterests)
    }

    private fun onInterestChecked(interest : Interest) {
        interestViewModel.addInterest(interest)
        changeButtonState(interestViewModel.count, interestViewModel.isValid)
    }

    private fun onInterestUnchecked(interest : Interest) {
        interestViewModel.removeInterest(interest)
        changeButtonState(interestViewModel.count, interestViewModel.isValid)
    }


    private fun handelEvents(event : Event<InterestViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is InterestViewModel.Navigation.ShowGetAll -> {
                    setInterestsLists(navigation.interests)
                    interestsPreferencesViewModel.getInterests()
                }
            }
        }
    }

    private fun handlePreferenceEvents(event : Event<InterestsPreferencesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is InterestsPreferencesViewModel.Navigation.ShowGetInterests -> {
                    interestViewModel.interests = navigation.interests
                    selectInterests(navigation.interests)
                    changeButtonState(interestViewModel.count, interestViewModel.isValid)
                }
                InterestsPreferencesViewModel.Navigation.ShowStoreInterests -> {
                    val action = InterestsFragmentDirections.actionInterestsFragmentToActivitiesFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun changeButtonState(count : Int, isModelValid : Boolean) {
        if (!isModelValid)
        {
            binding.nextButton.isEnabled = false
            binding.nextButton.text = getString(R.string.action_select)
        }
        else {
            binding.nextButton.isEnabled = true
            binding.nextButton.text = "${getString(R.string.action_select)} $count"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = InterestsFragment()
    }
}