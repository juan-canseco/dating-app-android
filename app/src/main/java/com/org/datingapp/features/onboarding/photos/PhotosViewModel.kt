package com.org.datingapp.features.onboarding.photos

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(private val getLocalUris: GetLocalPhotosUris, private val removePhoto: RemovePhoto) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    private var count = 0
    val isValid get() = count >= Constants.MinNumberOfPhotos

    var option : Options = Options.Add
    var position : Int = 0
    var photoUri : Uri = Uri.EMPTY

    fun getPhotos() {
        getLocalUris(UseCase.None(), viewModelScope) {
            val handleSuccess : (localUris : List<Uri>) -> Unit = { localUris ->
                count = localUris.size
                _events.value = Event(Navigation.ShowGetLocalUris(localUris))
            }
            it.fold({}, handleSuccess)
        }
    }

    fun removePhoto(position : Int) {
        removePhoto(RemovePhoto.Params(position), viewModelScope) {
            val handleSuccess : (UseCase.None) -> Unit = {
                getPhotos()
            }
            it.fold({}, handleSuccess)
        }
    }


    sealed class Options {
        object Add : Options()
        object Edit : Options()
    }

    sealed class Navigation {
        data class ShowGetLocalUris(val localUris : List<Uri>) : Navigation()
    }
}