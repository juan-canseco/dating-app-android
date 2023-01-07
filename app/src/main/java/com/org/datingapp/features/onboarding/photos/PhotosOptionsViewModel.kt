package com.org.datingapp.features.onboarding.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.org.datingapp.core.platform.Event

class PhotosOptionsViewModel : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events


    fun editPhoto(position : Int) {
        _events.value = Event(Navigation.ShowEditPhoto(position))
    }

    fun removePhoto(position : Int) {
        _events.value = Event(Navigation.ShowRemovePhoto(position))
    }

    sealed class Navigation {
        data class ShowEditPhoto(val position : Int) : Navigation()
        data class ShowRemovePhoto(val position : Int) : Navigation()
    }

}