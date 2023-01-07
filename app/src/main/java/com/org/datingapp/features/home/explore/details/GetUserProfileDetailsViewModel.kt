package com.org.datingapp.features.home.explore.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetUserProfileDetailsViewModel
@Inject
constructor(private val getUserProfileDetails: GetUserProfileDetails) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getDetails(userProfileId : String) {
        getUserProfileDetails(GetUserProfileDetails.Params(userProfileId), viewModelScope) {
            _events.value = Event(Navigation.ShowUserDetailsStart)
            val handleFailure : (failure : Failure) -> Unit = { failure ->
                when (failure) {
                    is GetUserProfileDetails.Failures.UserNotFound -> {
                        _events.value = Event(Navigation.ShowUserDetailsUserNotFoundError)
                    }
                    else -> {
                        _events.value = Event(Navigation.ShowUserDetailsError)
                    }
                }
            }
            val handleSuccess : (userProfileView : UserProfileDetailsView) -> Unit = { userProfileView ->
                _events.value = Event(Navigation.ShowUserDetailsSuccess(userProfileView))
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowUserDetailsStart : Navigation()
        data class ShowUserDetailsSuccess(val userProfileView: UserProfileDetailsView) : Navigation()
        object ShowUserDetailsUserNotFoundError : Navigation()
        object ShowUserDetailsError : Navigation()
    }
}