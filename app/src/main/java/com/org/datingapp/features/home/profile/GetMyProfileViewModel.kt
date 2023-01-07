package com.org.datingapp.features.home.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetMyProfileViewModel @Inject constructor(private val getMyProfile: GetMyProfile) : ViewModel() {

    private val _events = MutableLiveData<Event<GetMyProfileViewModelNavigation>?>()
    val events get() = _events

    fun getProfile() {
        _events.value = Event(GetMyProfileViewModelNavigation.ShowGetMyProfileStart)
        getMyProfile(UseCase.None(), viewModelScope) {
            it.fold(this::handleErrors, this::handleSuccess)
        }
    }

    private fun handleSuccess(profileView : ProfileView) {
        _events.value = Event(GetMyProfileViewModelNavigation.ShowGetMyProfileSuccess(profileView))
    }

    private fun handleErrors(failure : Failure) {
        _events.value = Event(GetMyProfileViewModelNavigation.ShowServerError)
    }

    sealed class GetMyProfileViewModelNavigation {
        object ShowGetMyProfileStart : GetMyProfileViewModelNavigation()
        data class ShowGetMyProfileSuccess(val profileView : ProfileView) : GetMyProfileViewModelNavigation()
        object ShowServerError : GetMyProfileViewModelNavigation()
    }
}