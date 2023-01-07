package com.org.datingapp.features.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.org.datingapp.R
import com.org.datingapp.core.platform.LoadingViewModel
import com.org.datingapp.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private val loadingViewModel by viewModels<LoadingViewModel>()

    private var _binding : FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firebaseAuth : FirebaseAuth
    private lateinit var googleSignInClient  : GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.googleSignInButton.setOnClickListener {
            if (!loadingViewModel.isLoading) {
                signIn()
            }
        }
        binding.emailSignInButton.setOnClickListener {
            if (!loadingViewModel.isLoading) {
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToSignInFragment()
                findNavController().navigate(action)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                handleExceptions(e)
            }
        }
    }
    private fun signIn() {
        loadingViewModel.isLoading = true
        showProgressButton()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken : String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                handleSuccess()
            }
            else if (task.exception != null){
                handleExceptions(task.exception!!)
            }
        }
    }

    private fun handleSuccess() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToMainNavigationGraph()
        findNavController().navigate(action)
    }

    private fun handleExceptions(exception : Exception) {
        exception.printStackTrace()
        loadingViewModel.isLoading = false
        hideProgressButton()
        Toast.makeText(context, R.string.failure_server_error, Toast.LENGTH_LONG).show()
    }

    private fun showProgressButton() {
        binding.googleSignInButton.showProgress {
            progressColor = Color.WHITE
            gravity = DrawableButton.GRAVITY_CENTER
            progressColor = R.color.colorPrimaryDark
        }
    }

    private fun hideProgressButton() {
        binding.googleSignInButton.hideProgress(R.string.label_sign_in_with_google)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AuthFragment"
        private const val RC_SIGN_IN = 9001
        @JvmStatic
        fun newInstance() = WelcomeFragment()
    }
}