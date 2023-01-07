package com.org.datingapp.features.auth.router

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouterViewModel @Inject constructor(private val router : Router): ViewModel() {

    private val _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events

    fun start() {
        router(UseCase.None(), viewModelScope) {
            it.fold(this::handleFailures, this::handleSuccess)
        }
    }

    private fun handleSuccess(none : UseCase.None) {
        _events.value = Event(Navigation.ShowUserLoggedIn)
    }
    private fun handleFailures(failure : Failure) {
        when(failure) {
            is RouterFailure.UserNotLoggedIn -> _events.value = Event(Navigation.ShowUserNotLoggedIn)
            is RouterFailure.UserProfileIncomplete -> _events.value = Event(Navigation.ShowUserProfileIncomplete)
            else -> {}
        }
    }

    sealed class Navigation {
        object ShowUserLoggedIn : Navigation()
        object ShowUserNotLoggedIn : Navigation()
        object ShowUserProfileIncomplete : Navigation()
    }
}