package com.org.datingapp.features.onboarding.interests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterestsPreferencesViewModel
@Inject
constructor(private val getInterestsPreference: GetInterestsPreference,
            private val storeInterestsPreferences: StoreInterestsPreferences) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getInterests() {
        getInterestsPreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (interest : HashSet<Interest>) -> Unit = { interests ->
                _events.value = Event(Navigation.ShowGetInterests(interests))
            }
            it.fold({}, handleSuccess)
        }
    }

    fun storeInterests(interests : HashSet<Interest>) {
        storeInterestsPreferences(StoreInterestsPreferences.Params(interests), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowStoreInterests)
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowStoreInterests : Navigation()
        data class ShowGetInterests(val interests : HashSet<Interest>) : Navigation()
    }
}