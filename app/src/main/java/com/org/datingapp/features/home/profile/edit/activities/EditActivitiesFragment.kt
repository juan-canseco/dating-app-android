package com.org.datingapp.features.home.profile.edit.activities

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.org.datingapp.R
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentEditActivitiesBinding
import com.org.datingapp.features.onboarding.activities.ActivitiesViewModel
import com.org.datingapp.features.onboarding.activities.ActivityAdapter
import com.org.datingapp.features.onboarding.activities.ActivityListItemView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivitiesFragment : Fragment() {

    private var _binding : FragmentEditActivitiesBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel by viewModels<ActivitiesViewModel>()
    private val getActivitiesViewModel by viewModels<GetActivitiesViewModel>()
    private val editActivitiesViewModel by viewModels<EditActivitiesViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()

    private lateinit var adapter : ActivityAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditActivitiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.editButton.setOnClickListener {

            if (loadingViewModel.isLoading)
                return@setOnClickListener

            if (activityViewModel.isValid) {
                editActivitiesViewModel.edit(activityViewModel.activities)
            }
        }
        activityViewModel.events.observe(viewLifecycleOwner, this::handelEvents)
        getActivitiesViewModel.events.observe(viewLifecycleOwner, this::handleGetEvents)
        editActivitiesViewModel.events.observe(viewLifecycleOwner, this::handleEditActivitiesEvents)
        activityViewModel.getActivities()
    }

    private fun setActivitiesLists(activities : List<ActivityListItemView>) {
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
                    getActivitiesViewModel.getActivities()
                }
            }
        }
    }

    private fun handleGetEvents(event : Event<GetActivitiesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GetActivitiesViewModel.Navigation.ShowGetActivities -> {
                    activityViewModel.activities = navigation.activities
                    selectActivities(navigation.activities)
                    changeButtonState(activityViewModel.count, activityViewModel.isValid)
                }
            }
        }
    }

    private fun handleEditActivitiesEvents(event : Event<EditActivitiesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                EditActivitiesViewModel.Navigation.ShowEditActivitiesStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.editButton)
                }
                EditActivitiesViewModel.Navigation.ShowEditActivitiesSuccess -> {
                    requireActivity().onBackPressed()
                }
                EditActivitiesViewModel.Navigation.ShowEditActivitiesError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.editButton)
                    Toast.makeText(requireContext(), R.string.failure_server_error_retry, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun changeButtonState(count : Int, isModelValid : Boolean) {
        if (!isModelValid)
        {
            binding.editButton.isEnabled = false
            binding.editButton.text = getString(R.string.action_select)
        }
        else {
            binding.editButton.isEnabled = true
            binding.editButton.text = "${getString(R.string.action_select)} $count"
        }
    }

    private fun showProgressBarCenter(button : Button) {
        button.showProgress {
            progressColor = Color.WHITE
            gravity = DrawableButton.GRAVITY_CENTER
        }
    }

    private fun disableProgressBarCenter(button : Button) {
        button.hideProgress(R.string.action_edit)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditActivitiesFragment()
    }
}