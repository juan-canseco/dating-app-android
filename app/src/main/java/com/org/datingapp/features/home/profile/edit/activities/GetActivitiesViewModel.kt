package com.org.datingapp.features.home.profile.edit.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.activities.GetActivitiesPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetActivitiesViewModel
@Inject
constructor(private val getActivitiesPreferences: GetActivitiesPreferences) : ViewModel(){

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getActivities() {
        getActivitiesPreferences(UseCase.None(), viewModelScope) {
            val handleSuccess : (activities : HashSet<Activity>) -> Unit = { activities ->
                _events.value = Event(Navigation.ShowGetActivities(activities))
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowGetActivities(val activities : HashSet<Activity>) : Navigation()
    }
}