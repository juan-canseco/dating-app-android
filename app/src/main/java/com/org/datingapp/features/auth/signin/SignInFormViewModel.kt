package com.org.datingapp.features.auth.signin

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.org.datingapp.BR
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInFormViewModel @Inject constructor(private val signInValidator : SignInValidator) : ObservableViewModel() {

    private val _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events

    private var isValidEmail = false
    @get:Bindable
    var email : String = ""
        set (value) {
            field = value
            isValidEmail = signInValidator.isValidEmail(field)
            val navigation = if (isValidEmail)
                Navigation.ShowValidEmail
            else
                Navigation.ShowInvalidEmail
            _events.value = Event(navigation)
            notifyPropertyChanged(BR.email)
            setValidCredentials()
        }

    private var isValidPassword = false
    @get:Bindable
    var password : String = ""
        set (value) {
            field = value
            isValidPassword = signInValidator.isValidPassword(field)
            val navigation = if (isValidPassword)
                Navigation.ShowValidPassword
            else
                Navigation.ShowInvalidPassword
            _events.value = Event(navigation)
            notifyPropertyChanged(BR.password)
            setValidCredentials()
        }


    private fun setValidCredentials() {
        val navigation = if (areCredentialsValid())
            Navigation.ShowValidCredentials
        else Navigation.ShowInvalidCredentials
        _events.value = Event(navigation)
    }


    private fun areCredentialsValid() = isValidEmail and isValidPassword

    sealed class Navigation {
        object ShowValidEmail : Navigation()
        object ShowInvalidEmail : Navigation()
        object ShowValidPassword : Navigation()
        object ShowInvalidPassword : Navigation()
        object ShowInvalidCredentials : Navigation()
        object ShowValidCredentials : Navigation()
    }
}