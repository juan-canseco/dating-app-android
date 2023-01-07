package com.org.datingapp.features.onboarding.username

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
class UsernamePreferencesViewModel @Inject constructor(private val getUsernamePreference: GetUsernamePreference,
                                                       private val storeUsernamePreference: StoreUsernamePreference): ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getUsername() {
        getUsernamePreference(UseCase.None(), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (username : String) -> Unit = { username ->
                _events.value = Event(Navigation.ShowGetUsername(username))
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    fun storeUsername(username : String) {
        storeUsernamePreference(StoreUsernamePreference.Params(username), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowStoreUsername)
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowGetUsername(val username : String) : Navigation()
        object ShowStoreUsername : Navigation()
    }
}