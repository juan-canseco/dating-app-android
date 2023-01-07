package com.org.datingapp.features.home.profile.edit.photos

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
class RemovePhotoViewModel
@Inject
constructor(private val remotePhoto: RemovePhoto) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events

    fun remove(position : Int) {
        remotePhoto(RemovePhoto.Params(position), viewModelScope) {
            val handleFailure : (Failure) -> Unit = {
                _events.value = Event(Navigation.ShowRemovePhotoFailure)
            }
            val handleSuccess : (UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowRemovePhotoSuccess)
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowRemovePhotoSuccess : Navigation()
        object ShowRemovePhotoFailure : Navigation()
    }
}