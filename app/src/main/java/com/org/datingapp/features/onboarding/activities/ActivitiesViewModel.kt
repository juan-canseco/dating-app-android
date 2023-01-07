package com.org.datingapp.features.onboarding.activities

import android.util.Log
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
class ActivitiesViewModel
@Inject
constructor(private val getAllActivities: GetAllActivities) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events


    fun getActivities() {
        getAllActivities(UseCase.None(), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (activities : List<Activity>) -> Unit = { activities ->
                Log.d("TAG1", activities.toString())
                val activitiesVm =
                    activities.map { activity ->
                        ActivityListItemView(
                            activity.id,
                            activity.name,
                            false)
                    }
                _events.value = Event(Navigation.ShowGetAll(activitiesVm))
            }
            it.fold(handleFailure, handleSuccess)
        }
    }


    private var _activities =  hashSetOf<Activity>()

    var activities
        get() = _activities
        set (value) {
            _activities.clear()
            _activities.addAll(value)
        }

    val isValid get() = _activities.size >= Constants.MinNumberOfActivities

    val count get() = _activities.size

    fun addActivity(activity : Activity) {
        if (_activities.contains(activity))
            return
        _activities.add(activity)
    }

    fun removeActivity(activity : Activity) {
        if (!_activities.contains(activity))
            return
        _activities.remove(activity)
    }

    sealed class Navigation {
        data class ShowGetAll(val activities: List<ActivityListItemView>) : Navigation()
    }
}