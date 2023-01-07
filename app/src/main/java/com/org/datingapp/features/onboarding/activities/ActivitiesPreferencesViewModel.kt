package com.org.datingapp.features.onboarding.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivitiesPreferencesViewModel
@Inject
constructor(private val getActivitiesPreferences: GetActivitiesPreferences,
            private val storeActivitiesPreferences: StoreActivitiesPreferences) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getActivities() {
        getActivitiesPreferences(UseCase.None(), viewModelScope) {
            val handleSuccess : (interest : HashSet<Activity>) -> Unit = { activities ->
                _events.value = Event(Navigation.ShowGetActivities(activities))
            }
            it.fold({}, handleSuccess)
        }
    }

    fun storeActivities(activities : HashSet<Activity>) {
        storeActivitiesPreferences(StoreActivitiesPreferences.Params(activities), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowStoreActivities)
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowStoreActivities : Navigation()
        data class ShowGetActivities(val activities : HashSet<Activity>) : Navigation()
    }

}