package com.org.datingapp.features.home.profile.edit.description

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.org.datingapp.core.platform.Event

@HiltViewModel
class EditDescriptionViewModel
@Inject
constructor(private val editDescription: EditDescription) : ViewModel() {
    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun edit(description: String) {
        _events.value = Event(Navigation.ShowEditDescriptionStart)
        editDescription(EditDescription.Params(description),viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowEditDescriptionSuccess)
            }
            val handleFailures : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowEditDescriptionError)
            }
            it.fold(handleFailures, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowEditDescriptionStart : Navigation()
        object ShowEditDescriptionSuccess : Navigation()
        object ShowEditDescriptionError : Navigation()
    }
}