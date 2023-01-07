package com.org.datingapp.features.home.profile.deatils

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
class GetProfileDetailsViewModel
@Inject
constructor(private val getProfileDetails: GetProfileDetails) : ViewModel() {

    private val _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getProfile() {
        _events.value = Event(Navigation.ShowGetProfileDetailsStart)
        getProfileDetails(UseCase.None(), viewModelScope) {
            it.fold(this::handleError, this::handleSuccess)
        }
    }

    private fun handleSuccess(profileDetails : ProfileDetailsView) {
        _events.value = Event(Navigation.ShowGetProfileDetailsSuccess(profileDetails))
    }

    private fun handleError(failure : Failure) {
        _events.value = Event(Navigation.ShowServerError)
    }

    sealed class Navigation {
        object ShowGetProfileDetailsStart : Navigation()
        data class ShowGetProfileDetailsSuccess(val profileDetails : ProfileDetailsView) : Navigation()
        object ShowServerError : Navigation()
    }
}