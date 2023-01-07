package com.org.datingapp.features.onboarding.birthdate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.domain.user.details.BirthDate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BirthDatePreferencesViewModel @Inject constructor(private val storeBirthDatePreference: StoreBirthDatePreference,
                                                        private val getBirthDatePreference: GetBirthDatePreference) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getBirthDate() {
        getBirthDatePreference(UseCase.None(), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (birthDate : BirthDate) -> Unit = { birthDate ->
                _events.value = Event(Navigation.ShowGetBirthDatePreference(birthDate))
            }
            it.fold(handleFailure, handleSuccess)
        }
    }


    fun storeBirthDate(birthDate: BirthDate) {
        storeBirthDatePreference(StoreBirthDatePreference.Params(birthDate), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowStoreBirthDatePreference)
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowGetBirthDatePreference(val birthDate : BirthDate) : Navigation()
        object ShowStoreBirthDatePreference : Navigation()
    }

}