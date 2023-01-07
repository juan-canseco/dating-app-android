package com.org.datingapp.features.auth.signin

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.*
import com.org.datingapp.databinding.FragmentSignInBinding
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.org.datingapp.R

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val emailSignInViewModel by viewModels<SignInViewModel>()
    private val credentialsViewModel by viewModels<SignInFormViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()

    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        bindProgressButton(binding.signInButton)
        binding.signInButton.attachTextChangeAnimator {
            fadeInMills = 300
            fadeOutMills = 300
        }
        binding.credentialsVm = credentialsViewModel
        binding.signInButton.setOnClickListener {
            if (!loadingViewModel.isLoading) {
                emailSignIn()
            }
        }
        binding.registerLabel.setOnClickListener {
            if (!loadingViewModel.isLoading) {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }
        emailSignInViewModel.events.observe(viewLifecycleOwner, this::handleSignInEvents)
        credentialsViewModel.events.observe(viewLifecycleOwner, this::handleCredentialsEvents)
    }

    private fun handleSignInEvents(event : Event<SignInViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is SignInViewModel.Navigation.ShowSignInStart -> {
                    showProgressBarCenter(binding.signInButton)
                    loadingViewModel.isLoading = true
                }
                is SignInViewModel.Navigation.ShowSignInSuccess -> {
                    loadingViewModel.isLoading = false
                    val action = SignInFragmentDirections.actionSignInFragmentToMainNavigationGraph()
                    findNavController().navigate(action)
                }
                is SignInViewModel.Navigation.ShowNetworkError -> {
                    disableProgressBarCenter(binding.signInButton)
                    loadingViewModel.isLoading = false
                    Toast.makeText(context, R.string.failure_network_connection, Toast.LENGTH_LONG).show()
                }
                is SignInViewModel.Navigation.ShowWrongCredentialsError -> {
                    disableProgressBarCenter(binding.signInButton)
                    loadingViewModel.isLoading = false
                    Toast.makeText(context, R.string.failure_sign_in_wrong_credentials, Toast.LENGTH_LONG).show()
                }
                is SignInViewModel.Navigation.ShowTooManyRequestsError -> {
                    disableProgressBarCenter(binding.signInButton)
                    loadingViewModel.isLoading = false
                    Toast.makeText(context, R.string.failure_sign_in_too_many_requests, Toast.LENGTH_LONG).show()
                }
                is SignInViewModel.Navigation.ShowServerError -> {
                    disableProgressBarCenter(binding.signInButton)
                    loadingViewModel.isLoading = false
                }
            }
        }
    }


    private fun handleCredentialsEvents(event : Event<SignInFormViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is SignInFormViewModel.Navigation.ShowValidEmail -> {
                    binding.email.endIconDrawable = requireActivity().getDrawable(R.drawable.ic_baseline_check_24)
                }
                is SignInFormViewModel.Navigation.ShowInvalidEmail -> {
                    binding.email.endIconDrawable = null
                }
                is SignInFormViewModel.Navigation.ShowValidPassword -> {
                    // do nothing
                }
                is SignInFormViewModel.Navigation.ShowInvalidPassword -> {
                    // do nothing
                }
                is SignInFormViewModel.Navigation.ShowValidCredentials -> {
                    binding.signInButton.isEnabled = true
                }
                is SignInFormViewModel.Navigation.ShowInvalidCredentials -> {
                    binding.signInButton.isEnabled = false
                }
            }
        }
    }

    private fun emailSignIn() {
        val email = credentialsViewModel.email
        val password = credentialsViewModel.password
        emailSignInViewModel.signIn(email, password)
    }

    private fun showProgressBarCenter(button : Button) {
        button.showProgress {
            progressColor = Color.WHITE
            gravity = DrawableButton.GRAVITY_CENTER
        }
    }

    private fun disableProgressBarCenter(button : Button) {
        button.hideProgress(R.string.action_done)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}