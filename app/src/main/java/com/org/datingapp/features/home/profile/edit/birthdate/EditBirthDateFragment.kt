package com.org.datingapp.features.home.profile.edit.birthdate

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.org.datingapp.R
import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentEditBirthDateBinding
import com.org.datingapp.features.onboarding.birthdate.BirthDateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditBirthDateFragment : Fragment() {

    private var _binding :  FragmentEditBirthDateBinding? = null
    private val binding get() = _binding!!
    private val birthDateViewModel by viewModels<BirthDateViewModel>()
    private val getBirthDateViewModel by viewModels<GetBirthDateViewModel>()
    private val editBirthDateViewModel by viewModels<EditBirthDateViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBirthDateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.editButton.setOnClickListener {
            if (loadingViewModel.isLoading) {
                return@setOnClickListener
            }

            if (!birthDateViewModel.isValid)
                return@setOnClickListener

            editBirthDateViewModel.edit(birthDateViewModel.birthDate)
        }

        birthDateViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        getBirthDateViewModel.events.observe(viewLifecycleOwner, this::handleGetBirthDateEvents)
        editBirthDateViewModel.events.observe(viewLifecycleOwner, this::handleEditBirthDateEvents)
        getBirthDateViewModel.getBirthDate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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


    private fun initDatePicker(birthDate : BirthDate) {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        if (birthDate != BirthDate.empty()) {
            year = birthDate.year
            month = birthDate.month - 1
            day = birthDate.day
        }
        binding.datePicker.init(year, month, day, this::onDateSelected)
    }

    private fun onDateSelected(dt : DatePicker?, year : Int, month : Int, day : Int) {
        val birthDate = BirthDate(year, month + 1, day)
        birthDateViewModel.setDate(birthDate)
    }


    private fun handleEditBirthDateEvents(event : Event<EditBirthDateViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                EditBirthDateViewModel.Navigation.ShowEditBirthDateStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.editButton)
                }
                EditBirthDateViewModel.Navigation.ShowEditBirthDateSuccess -> {
                    requireActivity().onBackPressed()
                }
                EditBirthDateViewModel.Navigation.ShowEditBirthDateError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.editButton)
                    Toast.makeText(requireContext(), R.string.failure_server_error_retry, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleGetBirthDateEvents(event : Event<GetBirthDateViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GetBirthDateViewModel.Navigation.ShowGetBirthDate -> {
                    initDatePicker(navigation.birthDate)
                    birthDateViewModel.setDate(navigation.birthDate)
                }
            }
        }
    }

    private fun handleEvents(event : Event<BirthDateViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is BirthDateViewModel.Navigation.ShowValidAge -> {
                    binding.editButton.isEnabled = true
                    binding.editButton.text = "${getString(R.string.label_on_boarding_birth_date_i_have)} ${navigation.age}"
                }
                is BirthDateViewModel.Navigation.ShowInvalidAge -> {
                    binding.editButton.isEnabled = false
                    binding.editButton.text = getString(R.string.action_next)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditBirthDateFragment()
    }
}