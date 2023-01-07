package com.org.datingapp.features.home.profile.edit.gender

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
import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentEditGenderBinding
import com.org.datingapp.features.onboarding.gender.GenderAdapter
import com.org.datingapp.features.onboarding.gender.GenderListItemViewModel
import com.org.datingapp.features.onboarding.gender.GendersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditGenderFragment : Fragment() {

    private var _binding : FragmentEditGenderBinding? = null
    private val binding get() = _binding!!
    private val gendersViewModel by viewModels<GendersViewModel>()
    private val getGenderViewModel by viewModels<GetGenderViewModel>()
    private val editGenderViewModel by viewModels<EditGenderViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()
    private lateinit var adapter : GenderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditGenderBinding.inflate(layoutInflater, container, false)
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

            if (gendersViewModel.isValid) {
                gendersViewModel.selectedGender?.let { gender ->
                    editGenderViewModel.edit(gender)
                }
            }
        }
        gendersViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        getGenderViewModel.events.observe(viewLifecycleOwner, this::handleGetGenderEvents)
        editGenderViewModel.events.observe(viewLifecycleOwner, this::handleEditGenderEvents)
        gendersViewModel.getGenders()
    }

    private fun setGendersRecycler(genders : List<GenderListItemViewModel>) {
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        adapter = GenderAdapter(genders) { gender -> onGenderClick(gender) }
        binding.gendersRecycler.adapter = adapter
        binding.gendersRecycler.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            binding.gendersRecycler.context,
            layoutManager.orientation
        )
        binding.gendersRecycler.addItemDecoration(dividerItemDecoration)
    }

    private fun onGenderClick(gender : Gender) {
        binding.editButton.isEnabled = true
        gendersViewModel.selectedGender = gender
    }

    private fun handleEvents(event : Event<GendersViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GendersViewModel.Navigation.ShowGetAll -> {
                    setGendersRecycler(navigation.genders)
                    getGenderViewModel.getGender()
                }
            }
        }
    }

    private fun handleGetGenderEvents(event : Event<GetGenderViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GetGenderViewModel.Navigation.ShowGetGender -> {
                    gendersViewModel.selectedGender = navigation.gender
                    adapter.select(navigation.gender)
                    binding.editButton.isEnabled = true
                }
            }
        }
    }

    private fun handleEditGenderEvents(event : Event<EditGenderViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                EditGenderViewModel.Navigation.ShowEditGenderStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.editButton)
                }
                EditGenderViewModel.Navigation.ShowEditGenderSuccess -> {
                    requireActivity().onBackPressed()
                }
                EditGenderViewModel.Navigation.ShowEditGenderError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.editButton)
                    Toast.makeText(requireContext(), R.string.failure_server_error_retry, Toast.LENGTH_LONG).show()
                }
            }
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
        fun newInstance() = EditGenderFragment()
    }
}