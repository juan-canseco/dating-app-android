package com.org.datingapp.features.auth.signout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel
@Inject constructor(private val signOutCase : SignOut) : ViewModel() {

    private val _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun signOut() {
        signOutCase(UseCase.None(), viewModelScope) {
            val handleSuccess : (useCase : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowSignOut)
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowSignOut : Navigation()
    }
}