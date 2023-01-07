package com.org.datingapp.features.home.profile.edit.photos

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.photos.Constants
import com.org.datingapp.features.onboarding.photos.GetLocalPhotosUris
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(private val getLocalUris: GetLocalPhotosUris, private val removePhoto: RemovePhoto) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    private var count = 0

    val hasOnlyOneItem get() = count == 1
    val isValid get() = count >= Constants.MinNumberOfPhotos

    var option : Options = Options.Add
    var position : Int = 0
    var photoUri : Uri = Uri.EMPTY

    // Refactor this later XD
    private lateinit var uris : List<Uri>


    // Only use this in Remove Photo Dialog
    // I need to think how to reimplement this
    fun getPhoto(position : Int) : Uri {
        return uris[position]
    }

    fun getPhotos() {
        getLocalUris(UseCase.None(), viewModelScope) {
            val handleSuccess : (localUris : List<Uri>) -> Unit = { localUris ->
                this.uris = localUris
                count = localUris.size
                _events.value = Event(Navigation.ShowGetLocalUris(localUris))
            }
            it.fold({}, handleSuccess)
        }
    }

    // Do Not Use this, I Know I HAve to Refactor a lot of things
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