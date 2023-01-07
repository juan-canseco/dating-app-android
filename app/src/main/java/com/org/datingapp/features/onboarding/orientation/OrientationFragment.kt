package com.org.datingapp.features.onboarding.orientation

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
import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentOrientationBinding
import com.org.datingapp.features.onboarding.StepsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrientationFragment : Fragment() {

    private var _binding : FragmentOrientationBinding? = null
    private val binding get() = _binding!!

    private val orientationViewModel by viewModels<OrientationViewModel>()
    private val orientationsPreferencesViewModel by viewModels<OrientationsPreferencesViewModel>()
    private val stepsViewModel by activityViewModels<StepsViewModel>()
    private lateinit var adapter : OrientationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrientationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepsViewModel.setStep(StepsViewModel.OrientationStep)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.nextButton.setOnClickListener {
            if (orientationViewModel.isValid) {
                orientationsPreferencesViewModel.storeOrientations(orientationViewModel.orientations)
            }
        }
        orientationViewModel.events.observe(viewLifecycleOwner, this::handelEvents)
        orientationsPreferencesViewModel.events.observe(viewLifecycleOwner, this::handlePreferenceEvents)
        orientationViewModel.getOrientations()
    }

    private fun setOrientationsLists(orientations : List<OrientationListItemViewModel>) {
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        adapter = OrientationAdapter(
            orientations,
            {orientation -> onOrientationChecked(orientation)} ,
            {orientation -> onOrientationUnchecked(orientation)})

        binding.orientationRecycler.adapter = adapter
        binding.orientationRecycler.layoutManager = layoutManager

        val dividerItemDecorator = DividerItemDecoration(
            binding.orientationRecycler.context,
            layoutManager.orientation
        )
        binding.orientationRecycler.addItemDecoration(dividerItemDecorator)
    }

    private fun selectOrientations(selectedOrientations: HashSet<Orientation>) {
        adapter.selectOrientations(selectedOrientations)
    }

    private fun onOrientationChecked(orientation : Orientation) {

        if (orientationViewModel.count == Constants.MaxNumberOfOrientations)
            return

        orientationViewModel.addOrientation(orientation)
        changeButtonState(orientationViewModel.count, orientationViewModel.isValid)
    }

    private fun onOrientationUnchecked(orientation : Orientation) {
        orientationViewModel.removeOrientation(orientation)
        changeButtonState(orientationViewModel.count, orientationViewModel.isValid)
    }


    private fun handelEvents(event : Event<OrientationViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is OrientationViewModel.Navigation.ShowGetAll -> {
                    setOrientationsLists(navigation.orientations)
                    orientationsPreferencesViewModel.getOrientations()
                }
            }
        }
    }

    private fun handlePreferenceEvents(event : Event<OrientationsPreferencesViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is OrientationsPreferencesViewModel.Navigation.ShowGetOrientations -> {
                    orientationViewModel.orientations = navigation.orientations
                    selectOrientations(navigation.orientations)
                    changeButtonState(orientationViewModel.count, orientationViewModel.isValid)
                }
                OrientationsPreferencesViewModel.Navigation.ShowStoreOrientations -> {
                    val action = OrientationFragmentDirections.actionOrientationFragmentToUsernameFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun changeButtonState(count : Int, isModelValid : Boolean) {
        if (!isModelValid)
        {
            binding.nextButton.isEnabled = false
            binding.nextButton.text =  getString(R.string.action_select)
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
        fun newInstance() = OrientationFragment()
    }
}