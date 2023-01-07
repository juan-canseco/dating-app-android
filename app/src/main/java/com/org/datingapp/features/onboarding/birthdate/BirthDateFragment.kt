package com.org.datingapp.features.onboarding.birthdate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.org.datingapp.R
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentBirthDateBinding
import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.features.onboarding.StepsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class BirthDateFragment : Fragment() {

    private var _binding : FragmentBirthDateBinding? = null
    private val binding get() = _binding!!
    private val birthDateViewModel by viewModels<BirthDateViewModel>()
    private val birthDatePreferencesViewModel by viewModels<BirthDatePreferencesViewModel>()
    private val stepsViewModel by activityViewModels<StepsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBirthDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.setStep(StepsViewModel.BirthDateStep)
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            if (birthDateViewModel.isValid) {
                birthDatePreferencesViewModel.storeBirthDate(birthDateViewModel.birthDate)
            }
        }
        birthDateViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        birthDatePreferencesViewModel.events.observe(viewLifecycleOwner, this::handlePreferenceEvents)
        birthDatePreferencesViewModel.getBirthDate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun onDateSelected(dt : DatePicker? ,year : Int, month : Int, day : Int) {
        val birthDate = BirthDate(year, month + 1, day)
        birthDateViewModel.setDate(birthDate)
    }

    private fun handlePreferenceEvents(event : Event<BirthDatePreferencesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is BirthDatePreferencesViewModel.Navigation.ShowGetBirthDatePreference -> {
                    initDatePicker(navigation.birthDate)
                    birthDateViewModel.setDate(navigation.birthDate)
                }
                BirthDatePreferencesViewModel.Navigation.ShowStoreBirthDatePreference -> {
                    val action = BirthDateFragmentDirections.actionBirthDateFragmentToGenderFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }


    private fun handleEvents(event : Event<BirthDateViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is BirthDateViewModel.Navigation.ShowValidAge -> {
                    binding.nextButton.isEnabled = true
                    binding.nextButton.text = "${getString(R.string.label_on_boarding_birth_date_i_have)} ${navigation.age}"
                }
                is BirthDateViewModel.Navigation.ShowInvalidAge -> {
                    binding.nextButton.isEnabled = false
                    binding.nextButton.text = getString(R.string.action_next)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BirthDateFragment()
    }
}