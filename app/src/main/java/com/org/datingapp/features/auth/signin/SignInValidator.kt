package com.org.datingapp.features.auth.signin

import androidx.core.util.PatternsCompat
import javax.inject.Inject

class SignInValidator @Inject constructor() {
    fun isValidEmail(email : String) : Boolean =  PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    fun isValidPassword(password : String) : Boolean = password.length > 5
}