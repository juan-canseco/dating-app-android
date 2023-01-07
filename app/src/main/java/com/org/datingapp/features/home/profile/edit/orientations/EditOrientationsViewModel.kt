package com.org.datingapp.features.home.profile.edit.orientations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditOrientationsViewModel
@Inject
constructor(private val editOrientations: EditOrientations) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun edit(orientations : HashSet<Orientation>) {
        _events.value = Event(Navigation.ShowEditOrientationsStart)
        editOrientations(EditOrientations.Params(orientations.toList()), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowEditOrientationsSuccess)
            }
            val handleFailures : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowEditOrientationsError)
            }
            it.fold(handleFailures, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowEditOrientationsStart : Navigation()
        object ShowEditOrientationsSuccess : Navigation()
        object ShowEditOrientationsError : Navigation()
    }
}