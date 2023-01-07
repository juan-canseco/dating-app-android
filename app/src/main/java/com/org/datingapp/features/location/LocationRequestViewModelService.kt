package com.org.datingapp.features.location

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
class LocationRequestViewModelService @Inject constructor(private val locationRequest: LocationRequest) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation?>>()
    val events : LiveData<Event<Navigation?>> get() = _events

    fun requestLocation() {
        _events.value = Event(Navigation.ShowStart)
        locationRequest(UseCase.None(), viewModelScope) {
            it.fold(this::handleFailures, this::handleSuccess)
        }
    }

    private fun handleSuccess(none : UseCase.None) {
        _events.value = Event(Navigation.ShowSuccess)
    }

    private fun handleFailures(failure : Failure) {
        when (failure) {
            is LocationRequest.Features.LocationPermissionNotGranted -> {
                _events.value = Event(Navigation.ShowLocationPermissionNotGranted)
            }
            else -> {

            }
        }
    }

    sealed class Navigation {
        object ShowStart : Navigation()
        object ShowSuccess : Navigation()
        object ShowLocationPermissionNotGranted : Navigation()
    }

}