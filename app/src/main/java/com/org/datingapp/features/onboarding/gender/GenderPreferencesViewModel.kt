package com.org.datingapp.features.onboarding.gender

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
class GenderPreferencesViewModel @Inject constructor(private val getGenderPreference: GetGenderPreference,
                                                     private val storeGenderPreference: StoreGenderPreference): ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getGender() {
        getGenderPreference(UseCase.None(), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (gender : Gender) -> Unit = { gender ->
                _events.value = Event(Navigation.ShowGetGenderPreference(gender))
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    fun storeGender(gender : Gender) {
        storeGenderPreference(StoreGenderPreference.Params(gender), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowStoreGenderPreference)
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowGetGenderPreference(val gender : Gender) : Navigation()
        object ShowStoreGenderPreference : Navigation()
    }

}
