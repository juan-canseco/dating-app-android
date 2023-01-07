package com.org.datingapp.features.onboarding.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.org.datingapp.R
import com.org.datingapp.databinding.FragmentRetryNetworkErrorDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RetryNetworkErrorDialogFragment : DialogFragment() {

    private var _binding : FragmentRetryNetworkErrorDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRetryNetworkErrorDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
    }

    override fun getTheme() : Int {
        return R.style.FullScreenDialogTheme
    }
}