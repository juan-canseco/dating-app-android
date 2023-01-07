package com.org.datingapp.features.home.profile.edit.name

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
class EditNameViewModel
@Inject
constructor(private val editName: EditName) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun edit(name : String) {
        _events.value = Event(Navigation.ShowEditNameStart)
        editName(EditName.Params(name), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowEditNameSuccess)
            }
            val handleFailures : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowEditNameError)
            }
            it.fold(handleFailures, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowEditNameStart : Navigation()
        object ShowEditNameSuccess : Navigation()
        object ShowEditNameError : Navigation()
    }
}

