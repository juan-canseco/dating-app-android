package com.org.datingapp.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(private val auth : FirebaseAuth) : ViewModel() {
    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    init {
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                _events.value = Event(Navigation.ShowSuccess)
            }
        }
    }

    fun signOut() {
        _events.value = Event(Navigation.ShowStart)
        auth.signOut()
    }

    sealed class Navigation {
        object ShowStart : Navigation()
        object ShowSuccess : Navigation()
    }
}