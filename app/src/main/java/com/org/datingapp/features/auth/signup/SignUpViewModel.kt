package com.org.datingapp.features.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.auth.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUp : SignUp) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>>()
    val events get () = _events

    fun signUp(email : String, password : String) {
        _events.value = Event(Navigation.ShowSignUpStart)
        signUp(SignUp.Params(email, password), viewModelScope) {
            it.fold(this::handleFailure, this::handleSuccess)
        }
    }

    private fun handleSuccess(none : UseCase.None) {
        _events.value = Event(Navigation.ShowSignUpSuccess)
    }

    private fun handleFailure(failure : Failure) {
        when (failure) {
            is Authenticator.Failures.EmailCollision -> _events.value = Event(Navigation.ShowEmailAlreadyExistsError)
            is Failure.NetworkConnection -> _events.value = Event(Navigation.ShowNetworkError)
            else -> _events.value = Event(Navigation.ShowServerError)
        }
    }

    sealed class Navigation {
        object ShowSignUpStart : Navigation()
        object ShowSignUpSuccess : Navigation()
        object ShowEmailAlreadyExistsError : Navigation()
        object ShowServerError : Navigation()
        object ShowNetworkError : Navigation()
    }
}