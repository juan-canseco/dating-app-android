package com.org.datingapp.features.home.profile.edit.interests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.interests.GetInterestsPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetInterestsViewModel
@Inject
constructor(private val getInterestsPreference: GetInterestsPreference) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getInterests() {
        getInterestsPreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (interests : HashSet<Interest>) -> Unit = { interests ->
                _events.value = Event(Navigation.ShowGetInterests(interests))
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowGetInterests(val interests : HashSet<Interest>) : Navigation()
    }
}