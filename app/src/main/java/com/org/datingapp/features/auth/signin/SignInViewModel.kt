package com.org.datingapp.features.auth.signin

import androidx.lifecycle.LiveData
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
class SignInViewModel @Inject constructor(private val signIn : SignIn) : ViewModel() {

    private val _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun signIn(email : String, password : String) {
        _events.value = Event(Navigation.ShowSignInStart)
        val handleSuccess : (useCase : UseCase.None) -> Unit = {
            _events.value = Event(Navigation.ShowSignInSuccess)
        }
        val handleFailure : (failure : Failure) -> Unit = { failure ->
            when (failure) {
                is Authenticator.Failures.WrongCredentials -> _events.value = Event(Navigation.ShowWrongCredentialsError)
                is Authenticator.Failures.TooManyRequest -> _events.value = Event(Navigation.ShowTooManyRequestsError)
                is Failure.NetworkConnection -> _events.value = Event(Navigation.ShowNetworkError)
                else -> _events.value = Event(Navigation.ShowServerError)
            }
        }
        signIn(SignIn.Params(email, password), viewModelScope) {
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowSignInStart : Navigation()
        object ShowSignInSuccess : Navigation()
        object ShowNetworkError : Navigation()
        object ShowWrongCredentialsError : Navigation()
        object ShowTooManyRequestsError : Navigation()
        object ShowServerError : Navigation()
    }
}