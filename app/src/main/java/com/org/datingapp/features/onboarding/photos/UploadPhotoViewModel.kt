package com.org.datingapp.features.onboarding.photos

import android.net.Uri
import androidx.lifecycle.*
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadPhotoViewModel @Inject constructor(private val uploadPhoto: AddPhoto,
                                               private val editPhoto : EditPhoto) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events

    private var _added = false
    private var _edited = false

    fun add(photoUri : Uri) {
        if (_added) return
        uploadPhoto(AddPhoto.Params(photoUri), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowUploadPhotoFailure)
            }
            val handleSuccess : (UseCase.None) -> Unit = {
                _added = true
                _events.value = Event(Navigation.ShowUploadPhotoSuccess)
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    fun edit(photoUri : Uri, photoPosition : Int) {
        if (_edited) return
        editPhoto(EditPhoto.Params(photoUri, photoPosition), viewModelScope) {
            val handleFailure : (Failure) -> Unit = {
                _events.value = Event(Navigation.ShowUploadPhotoFailure)
            }
            val handleSuccess : (UseCase.None) -> Unit = {
                _edited = true
                _events.value = Event(Navigation.ShowUploadPhotoSuccess)
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowUploadPhotoSuccess : Navigation()
        object ShowUploadPhotoFailure : Navigation()
    }
}