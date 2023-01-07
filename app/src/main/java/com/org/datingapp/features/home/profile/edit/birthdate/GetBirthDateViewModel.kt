package com.org.datingapp.features.home.profile.edit.birthdate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.birthdate.GetBirthDatePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetBirthDateViewModel
@Inject
constructor(private val getBirthDatePreference: GetBirthDatePreference) : ViewModel(){

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getBirthDate() {
        getBirthDatePreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (birthDate : BirthDate) -> Unit = { birthDate ->
                _events.value = Event(Navigation.ShowGetBirthDate(birthDate))
            }
            it.fold({}, handleSuccess)
        }
    }


    sealed class Navigation {
        data class ShowGetBirthDate(val birthDate: BirthDate) : Navigation()
    }
}