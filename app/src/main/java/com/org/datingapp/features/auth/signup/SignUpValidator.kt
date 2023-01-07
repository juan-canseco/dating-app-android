package com.org.datingapp.features.auth.signup

import android.text.TextUtils
import androidx.core.util.PatternsCompat
import javax.inject.Inject

class SignUpValidator @Inject constructor() {

    fun isValidEmail(email : String) : Boolean {
        return !TextUtils.isEmpty(email) &&
                PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() &&
                email.length in 10..75
    }

    fun isValidPassword(password : String) : Boolean {
        return password.length in 5..30
    }

    fun isValidPasswordConfirmation(password : String, passwordConfirmation : String) : Boolean {
        return passwordConfirmation == password && isValidPassword(passwordConfirmation)
    }
}