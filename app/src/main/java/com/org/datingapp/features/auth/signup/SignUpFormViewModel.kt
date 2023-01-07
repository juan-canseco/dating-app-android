package com.org.datingapp.features.auth.signup

import android.text.TextUtils
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.org.datingapp.BR
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpFormViewModel @Inject constructor(private val signUpValidator : SignUpValidator) : ObservableViewModel() {


    private val _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events


    private var isEmailValid = false
    private var isPasswordValid = false
    private var isConfirmationPasswordValid = false

    @get:Bindable
    var email : String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
            isEmailValid = signUpValidator.isValidEmail(field)
            if (isEmailValid) _events.value = Event(Navigation.ShowValidEmail)
            else _events.value = Event(Navigation.ShowInvalidEmail)
            _events.value = Event(Navigation.ShowClearEmailErrors)
        }

    @get:Bindable
    var password : String = ""
        set (value) {
            field = value
            notifyPropertyChanged(BR.password)
            isPasswordValid = signUpValidator.isValidPassword(field)
            if (isPasswordValid) {
                _events.value = Event(Navigation.ShowValidPassword)
                isConfirmationPasswordValid = signUpValidator.isValidPasswordConfirmation(passwordConfirmation, field)
                if (isConfirmationPasswordValid)
                    _events.value = Event(Navigation.ShowValidPasswordConfirmation)
                else
                    _events.value = Event(Navigation.ShowInvalidPasswordConfirmation)
            }
            else _events.value = Event(Navigation.ShowInvalidPassword)
            _events.value = Event(Navigation.ShowClearPasswordErrors)
        }

    @get:Bindable
    var passwordConfirmation : String = ""
        set (value) {
            field = value
            notifyPropertyChanged(BR.passwordConfirmation)
            isConfirmationPasswordValid = signUpValidator.isValidPasswordConfirmation(password, field)
            if (isConfirmationPasswordValid) _events.value = Event(Navigation.ShowValidPasswordConfirmation)
            else _events.value = Event(Navigation.ShowInvalidPasswordConfirmation)
            _events.value = Event(Navigation.ShowClearPasswordConfirmationErrors)
        }

    fun validate() : Boolean {

        val isEmailEmpty = TextUtils.isEmpty(email)
        val isPasswordEmpty = TextUtils.isEmpty(password)
        val isConfirmationPasswordEmpty = TextUtils.isEmpty(passwordConfirmation)

        if (isEmailEmpty) _events.value = Event(Navigation.ShowEmailRequiredError)
        else if (!isEmailValid) _events.value = Event(Navigation.ShowEmailError)

        if (isPasswordEmpty) _events.value = Event(Navigation.ShowPasswordRequiredError)
        else if (!isPasswordValid) _events.value = Event(Navigation.ShowPasswordError)

        if (isConfirmationPasswordEmpty) _events.value = Event(Navigation.ShowPasswordConfirmationRequiredError)
        else if (!isConfirmationPasswordValid) _events.value = Event(Navigation.ShowPasswordConfirmationError)

        return isEmailValid and isPasswordValid and isConfirmationPasswordValid
    }


    sealed class Navigation {

        object ShowValidEmail : Navigation()
        object ShowInvalidEmail : Navigation()
        object ShowEmailRequiredError : Navigation()
        object ShowEmailError : Navigation()
        object ShowClearEmailErrors : Navigation()

        object ShowValidPassword : Navigation()
        object ShowInvalidPassword : Navigation()
        object ShowPasswordRequiredError : Navigation()
        object ShowPasswordError : Navigation()
        object ShowClearPasswordErrors : Navigation()

        object ShowValidPasswordConfirmation : Navigation()
        object ShowInvalidPasswordConfirmation : Navigation()
        object ShowPasswordConfirmationError : Navigation()
        object ShowPasswordConfirmationRequiredError : Navigation()
        object ShowClearPasswordConfirmationErrors : Navigation()
    }
}