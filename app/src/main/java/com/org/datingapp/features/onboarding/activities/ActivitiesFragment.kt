package com.org.datingapp.features.onboarding.activities

import android.os.Bundle
import android.util.Log
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
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentActivitesBinding
import com.org.datingapp.features.onboarding.StepsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivitiesFragment : Fragment() {


    private var _binding : FragmentActivitesBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel by viewModels<ActivitiesViewModel>()
    private val activitiesPreferencesViewModel by viewModels<ActivitiesPreferencesViewModel>()
    private val stepsViewModel by activityViewModels<StepsViewModel>()
    private lateinit var adapter : ActivityAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.setStep(StepsViewModel.ActivitiesStep)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.nextButton.setOnClickListener {
            if (activityViewModel.isValid) {
                activitiesPreferencesViewModel.storeActivities(activityViewModel.activities)
            }
        }
        activityViewModel.events.observe(viewLifecycleOwner, this::handelEvents)
        activitiesPreferencesViewModel.events.observe(viewLifecycleOwner, this::handlePreferenceEvents)
        activityViewModel.getActivities()
    }

    private fun setActivitiesLists(activities : List<ActivityListItemView>) {
        Log.d("TAG1", activities.toString())
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        adapter = ActivityAdapter(
            activities,
            {activity -> onActivityChecked(activity)} ,
            {activity -> onActivityUnchecked(activity)})

        binding.activitiesRecycler.adapter = adapter
        binding.activitiesRecycler.layoutManager = layoutManager

        val dividerItemDecorator = DividerItemDecoration(
            binding.activitiesRecycler.context,
            layoutManager.orientation
        )
        binding.activitiesRecycler.addItemDecoration(dividerItemDecorator)
    }

    private fun selectActivities(selectedActivities: HashSet<Activity>) {
        adapter.selectActivities(selectedActivities)
    }

    private fun onActivityChecked(activity : Activity) {
        activityViewModel.addActivity(activity)
        changeButtonState(activityViewModel.count, activityViewModel.isValid)
    }

    private fun onActivityUnchecked(activity : Activity) {
        activityViewModel.removeActivity(activity)
        changeButtonState(activityViewModel.count, activityViewModel.isValid)
    }


    private fun handelEvents(event : Event<ActivitiesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ActivitiesViewModel.Navigation.ShowGetAll -> {
                    setActivitiesLists(navigation.activities)
                    activitiesPreferencesViewModel.getActivities()
                }
            }
        }
    }

    private fun handlePreferenceEvents(event : Event<ActivitiesPreferencesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ActivitiesPreferencesViewModel.Navigation.ShowGetActivities -> {
                    activityViewModel.activities = navigation.activities
                    selectActivities(navigation.activities)
                    changeButtonState(activityViewModel.count, activityViewModel.isValid)
                }
                ActivitiesPreferencesViewModel.Navigation.ShowStoreActivities -> {
                    val action = ActivitiesFragmentDirections.actionActivitiesFragmentToPhotosFragment()
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
        fun newInstance() = ActivitiesFragment()
    }
}