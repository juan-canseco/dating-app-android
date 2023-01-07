package com.org.datingapp.features.auth.signup

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
import com.org.datingapp.databinding.FragmentSignUpBinding
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.LoadingViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.org.datingapp.R

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val signUpFormViewModel by viewModels<SignUpFormViewModel>()
    private val signUpViewModel by viewModels<SignUpViewModel>()
    private val loadingViewModel by viewModels<LoadingViewModel>()

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        bindProgressButton(binding.signUpButton)
        binding.signUpButton.attachTextChangeAnimator {
            fadeInMills = 300
            fadeOutMills = 300
        }
        binding.signUpButton.setOnClickListener {
            if (!loadingViewModel.isLoading)
                signUp()
        }
        binding.vm = signUpFormViewModel
        signUpFormViewModel.events.observe(viewLifecycleOwner, this::handleValidationEvents)
        signUpViewModel.events.observe(viewLifecycleOwner, this::handleEvents)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleEvents(event : Event<SignUpViewModel.Navigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is SignUpViewModel.Navigation.ShowSignUpStart -> {
                    loadingViewModel.isLoading = true
                    showProgressBarCenter(binding.signUpButton)
                }
                is SignUpViewModel.Navigation.ShowSignUpSuccess -> {
                    val action = SignUpFragmentDirections.actionSignUpFragmentToMainNavigationGraph()
                    findNavController().navigate(action)
                }
                SignUpViewModel.Navigation.ShowNetworkError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.signUpButton)
                    Toast.makeText(context, R.string.failure_network_connection, Toast.LENGTH_LONG).show()
                }
                SignUpViewModel.Navigation.ShowServerError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.signUpButton)
                    Toast.makeText(context, R.string.failure_server_error, Toast.LENGTH_LONG).show()
                }
                is SignUpViewModel.Navigation.ShowEmailAlreadyExistsError -> {
                    loadingViewModel.isLoading = false
                    disableProgressBarCenter(binding.signUpButton)
                    binding.email.error = getString(R.string.failure_sign_up_email_already_in_use)
                }

            }
        }
    }

    private fun handleValidationEvents(event : Event<SignUpFormViewModel.Navigation>?) {
        event?.getContentIfNotHandled().let { errors ->
            when (errors) {
                is SignUpFormViewModel.Navigation.ShowValidEmail -> {
                   binding.email.endIconDrawable = requireActivity().getDrawable(R.drawable.ic_baseline_check_24)
                }
                is SignUpFormViewModel.Navigation.ShowInvalidEmail -> {
                    binding.email.endIconDrawable = null
                }
                is SignUpFormViewModel.Navigation.ShowEmailError -> {
                    binding.email.error = getString(R.string.failure_sign_up_invalid_email)
                }
                is SignUpFormViewModel.Navigation.ShowEmailRequiredError -> {
                   binding.email.error = getString(R.string.failure_sign_up_email_required)
                }
                is SignUpFormViewModel.Navigation.ShowClearEmailErrors -> {
                    binding.email.error = null
                }
                is SignUpFormViewModel.Navigation.ShowValidPassword -> {
                    binding.password.endIconDrawable = requireActivity().getDrawable(R.drawable.ic_baseline_check_24)
                }
                is SignUpFormViewModel.Navigation.ShowInvalidPassword -> {
                    binding.password.endIconDrawable = null
                }
                is SignUpFormViewModel.Navigation.ShowPasswordError -> {
                    binding.password.error = getString(R.string.failure_sign_up_invalid_password)
                }
                is SignUpFormViewModel.Navigation.ShowPasswordRequiredError -> {
                   binding.password.error = getString(R.string.failure_sign_up_password_required)
                }
                is SignUpFormViewModel.Navigation.ShowClearPasswordErrors -> {
                    binding.password.error = null
                }
                is SignUpFormViewModel.Navigation.ShowValidPasswordConfirmation -> {
                    binding.confirmationPassword.endIconDrawable = requireActivity().getDrawable(R.drawable.ic_baseline_check_24)
                }
                is SignUpFormViewModel.Navigation.ShowInvalidPasswordConfirmation -> {
                    binding.confirmationPassword.endIconDrawable = null
                }
                is SignUpFormViewModel.Navigation.ShowPasswordConfirmationError -> {
                    binding.confirmationPassword.error = getString(R.string.failure_sign_up_invalid_password_confirmation)
                }
                is SignUpFormViewModel.Navigation.ShowPasswordConfirmationRequiredError -> {
                    binding.confirmationPassword.error = getString(R.string.failure_sign_up_password_confirmation_required)
                }
                is SignUpFormViewModel.Navigation.ShowClearPasswordConfirmationErrors -> {
                    binding.confirmationPassword.error = null
                }
            }
        }
    }
    private fun signUp() {
        if (signUpFormViewModel.validate()) {
            signUpViewModel.signUp(signUpFormViewModel.email, signUpFormViewModel.password)
        }
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

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}