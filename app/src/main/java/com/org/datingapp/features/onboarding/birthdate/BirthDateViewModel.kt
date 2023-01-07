package com.org.datingapp.features.onboarding.birthdate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.domain.user.details.BirthDate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BirthDateViewModel @Inject constructor(private val computeAge : ComputeAge)  : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events

    private var _isValid = false
    val isValid get() = _isValid

    private var _birthDate = BirthDate.empty()
    val birthDate get() = _birthDate

    private val handleFailure : (failure : Failure) -> Unit = {
        _isValid = false
        _birthDate = BirthDate.empty()
        _events.value = Event(Navigation.ShowInvalidAge)
    }
    private val handleSuccess : (result : ComputeAge.Result) -> Unit = { result ->
        _isValid = true
        _birthDate = result.birthDate
        _events.value = Event(Navigation.ShowValidAge(result.birthDate, result.age))
    }

    fun setDate(birthDate : BirthDate) {
        computeAge(ComputeAge.Params(birthDate), viewModelScope) {
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowValidAge(val birthDate : BirthDate, val age : Int) : Navigation()
        object ShowInvalidAge : Navigation()
    }
}