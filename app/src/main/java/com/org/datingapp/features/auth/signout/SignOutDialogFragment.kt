package com.org.datingapp.features.auth.signout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.org.datingapp.R
import com.org.datingapp.core.platform.Event
import com.org.datingapp.databinding.FragmentSignOutDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignOutDialogFragment : DialogFragment() {

    private var _binding : FragmentSignOutDialogBinding? = null
    private val binding get() = _binding!!

    private val signOutViewModel by viewModels<SignOutViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignOutDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        signOutViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
        signOutViewModel.signOut()
    }

    private fun handleEvents(event : Event<SignOutViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                SignOutViewModel.Navigation.ShowSignOut -> {
                    val intent = requireActivity().intent
                    requireActivity().finish()
                    startActivity(intent)
                }
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}