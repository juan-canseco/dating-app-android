package com.org.datingapp.features.home.profile.edit.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EditActivitiesViewModel
@Inject
constructor(private val editActivities: EditActivities) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun edit(activities : HashSet<Activity>) {
        _events.value = Event(Navigation.ShowEditActivitiesStart)
        editActivities(EditActivities.Params(activities.toList()), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowEditActivitiesSuccess)
            }
            val handleFailures : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowEditActivitiesError)
            }
            it.fold(handleFailures, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowEditActivitiesStart : Navigation()
        object ShowEditActivitiesSuccess : Navigation()
        object ShowEditActivitiesError : Navigation()
    }
}
