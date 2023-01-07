package com.org.datingapp.features.home.explore

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
class GetUserProfilesViewModel
@Inject
constructor(private val getUserProfiles: GetUserProfiles) : ViewModel() {


    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getProfiles() {
        getUserProfiles(UseCase.None(), viewModelScope) {
            _events.value = Event(Navigation.ShowGetUserProfilesStart)
            val handleFailure : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowGetUserProfilesError)
            }
            val handleSuccess : (userProfiles : List<UserProfileView>) -> Unit = { userProfiles ->
                val event = if (userProfiles.isEmpty()) {
                    Event(Navigation.ShowGetUserProfilesEmptyList)
                }
                else {
                    Event(Navigation.ShowGetUserProfilesSuccess(userProfiles))
                }
                _events.value = event
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowGetUserProfilesStart : Navigation()
        data class ShowGetUserProfilesSuccess(val userProfiles : List<UserProfileView>) : Navigation()
        object ShowGetUserProfilesEmptyList : Navigation()
        object ShowGetUserProfilesError : Navigation()
    }

}