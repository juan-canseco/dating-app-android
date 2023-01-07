package com.org.datingapp.features.home.profile.edit.interests

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
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentEditInterestsBinding
import com.org.datingapp.features.onboarding.interests.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditInterestsFragment : Fragment() {

    private var _binding : FragmentEditInterestsBinding? = null
    private val binding get() = _binding!!

    private val interestViewModel by viewModels<InterestViewModel>()
    private val getInterestsViewModel by viewModels<GetInterestsViewModel>()
    private val editInterestsViewModel by viewModels<EditInterestsViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()

    private lateinit var adapter : InterestAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditInterestsBinding.inflate(layoutInflater, container, false)
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

            if (interestViewModel.isValid) {
                editInterestsViewModel.edit(interestViewModel.interests)
            }
        }
        interestViewModel.events.observe(viewLifecycleOwner, this::handelEvents)
        getInterestsViewModel.events.observe(viewLifecycleOwner, this::handleGetEvents)
        editInterestsViewModel.events.observe(viewLifecycleOwner, this::handleEditInterestsEvents)
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
                   getInterestsViewModel.getInterests()
                }
            }
        }
    }

    private fun handleGetEvents(event : Event<GetInterestsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GetInterestsViewModel.Navigation.ShowGetInterests -> {
                    interestViewModel.interests = navigation.interests
                    selectInterests(navigation.interests)
                    changeButtonState(interestViewModel.count, interestViewModel.isValid)
                }
            }
        }
    }


    private fun handleEditInterestsEvents(event : Event<EditInterestsViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                EditInterestsViewModel.Navigation.ShowEditInterestsStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.editButton)
                }
                EditInterestsViewModel.Navigation.ShowEditInterestsSuccess -> {
                    requireActivity().onBackPressed()
                }
                EditInterestsViewModel.Navigation.ShowEditInterestsError -> {
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
        fun newInstance() = EditInterestsFragment()
    }
}