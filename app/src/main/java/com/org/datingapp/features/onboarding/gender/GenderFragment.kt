package com.org.datingapp.features.onboarding.gender

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
import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentGenderBinding
import com.org.datingapp.features.onboarding.StepsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenderFragment : Fragment() {

    private var _binding : FragmentGenderBinding? = null
    private val binding get() = _binding!!
    private val genderViewModel by viewModels<GendersViewModel>()
    private val genderPreferencesViewModel by viewModels<GenderPreferencesViewModel>()
    private val stepsViewModel by activityViewModels<StepsViewModel>()
    private lateinit var adapter : GenderAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.setStep(StepsViewModel.GenderStep)
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.nextButton.setOnClickListener {
            if (genderViewModel.isValid) {
                genderViewModel.selectedGender?.let { gender ->
                    genderPreferencesViewModel.storeGender(gender)
                }
            }
        }
        genderViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        genderPreferencesViewModel.events.observe(viewLifecycleOwner, this::handlePreferenceEvents)
        genderViewModel.getGenders()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.nextButton.isEnabled = true
        genderViewModel.selectedGender = gender
    }

    private fun handleEvents(event : Event<GendersViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GendersViewModel.Navigation.ShowGetAll -> {
                    setGendersRecycler(navigation.genders)
                    genderPreferencesViewModel.getGender()
                }
            }
        }
    }

    private fun handlePreferenceEvents(event : Event<GenderPreferencesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is GenderPreferencesViewModel.Navigation.ShowGetGenderPreference -> {
                    if (navigation.gender == Gender.empty())
                        return@let

                    genderViewModel.selectedGender = navigation.gender
                    adapter.select(navigation.gender)
                    binding.nextButton.isEnabled = true
                }
                GenderPreferencesViewModel.Navigation.ShowStoreGenderPreference -> {
                    val action = GenderFragmentDirections.actionGenderFragmentToOrientationFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = GenderFragment()
    }
}