package com.org.datingapp.features.home.profile.edit.birthdate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditBirthDateViewModel
@Inject constructor(private val editBirthDate: EditBirthDate) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun edit(birthDate: BirthDate) {
        _events.value = Event(Navigation.ShowEditBirthDateStart)
        editBirthDate(EditBirthDate.Params(birthDate), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowEditBirthDateSuccess)
            }
            val handleFailures : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowEditBirthDateError)
            }
            it.fold(handleFailures, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowEditBirthDateStart : Navigation()
        object ShowEditBirthDateSuccess : Navigation()
        object ShowEditBirthDateError : Navigation()
    }
}