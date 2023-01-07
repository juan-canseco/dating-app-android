package com.org.datingapp.features.home.profile.edit.gender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.gender.GetGenderPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetGenderViewModel
@Inject
constructor(private val getGenderPreference: GetGenderPreference): ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getGender() {
        getGenderPreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (gender : Gender) -> Unit = { gender ->
                _events.value = Event(Navigation.ShowGetGender(gender))
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowGetGender(val gender : Gender) : Navigation()
    }
}