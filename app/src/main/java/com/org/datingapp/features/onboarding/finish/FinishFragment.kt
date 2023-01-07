package com.org.datingapp.features.onboarding.finish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.org.datingapp.core.platform.ConnectivityManager
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentFinishBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FinishFragment : Fragment() {

    private var _binding : FragmentFinishBinding? = null
    private val binding get() = _binding!!

    private val finishViewModel by viewModels<FinishViewModel>()

    @Inject
    lateinit var connectivityManager : ConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFinishBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        finishViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
    }

    override fun onStart() {
        super.onStart()
        //connectivityManager.registerConnectionObserver(viewLifecycleOwner, this::handleConnectivityChange)
        finishViewModel.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //connectivityManager.unregisterConnectionObserver(viewLifecycleOwner)
    }


    private fun goToNetworkErrorDialog() {
        val action = FinishFragmentDirections.actionFinishFragmentToRetryNetworkErrorDialogFragment()
        findNavController().navigate(action)
    }

    private fun handleConnectivityChange(internetAvailable : Boolean) {
        if (!internetAvailable) {
            goToNetworkErrorDialog()
        }
        else {

        }
    }

    private fun handleEvents(event : Event<FinishViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                FinishViewModel.Navigation.ShowFinishSuccess -> {
                    val intent = requireActivity().intent
                    requireActivity().finish()
                    startActivity(intent)
                }
                FinishViewModel.Navigation.ShowNetworkError -> {

                }
                FinishViewModel.Navigation.ShowServerError -> {
                    // Show server error layout
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FinishFragment()
    }
}