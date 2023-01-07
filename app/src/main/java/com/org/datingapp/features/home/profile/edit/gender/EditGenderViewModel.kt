package com.org.datingapp.features.home.profile.edit.gender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditGenderViewModel
@Inject
constructor(private val editGender: EditGender) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun edit(gender : Gender) {
        _events.value = Event(Navigation.ShowEditGenderStart)
        editGender(EditGender.Params(gender), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowEditGenderSuccess)
            }
            val handleFailures : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowEditGenderError)
            }
            it.fold(handleFailures, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowEditGenderStart : Navigation()
        object ShowEditGenderSuccess : Navigation()
        object ShowEditGenderError : Navigation()
    }
}