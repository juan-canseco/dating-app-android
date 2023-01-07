package com.org.datingapp.features.home.profile.edit

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
class GetProfileEditDetailsViewModel
@Inject
constructor(private val getProfileEditDetails: GetProfileEditDetails)  : ViewModel() {

    private val _events  = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getProfileDetails() {
        _events.value = Event(Navigation.ShowGetProfileEditDetailsStart)
        getProfileEditDetails(UseCase.None(), viewModelScope) {
            it.fold(this::handleError, this::handleSuccess)
        }
    }

    private fun handleSuccess(profileEditDetails : ProfileEditDetailsView) {
        _events.value = Event(Navigation.ShowGetProfileEditDetailsSuccess(profileEditDetails))
    }

    private fun handleError(failure : Failure) {
        _events.value = Event(Navigation.ShowServerError)
    }

    sealed class Navigation {
        object ShowGetProfileEditDetailsStart : Navigation()
        data class ShowGetProfileEditDetailsSuccess(val profileEditDetailsView  : ProfileEditDetailsView) : Navigation()
        object ShowServerError : Navigation()
    }
}
