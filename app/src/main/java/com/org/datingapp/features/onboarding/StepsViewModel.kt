package com.org.datingapp.features.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.org.datingapp.core.platform.Event

class StepsViewModel : ViewModel() {

    private val numberOfSteps = 9

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun setStep(step : Int) {
        val progress = (step * 100) / numberOfSteps
        _events.value = Event(Navigation.ShowProgressChange(progress))
    }

    fun hideProgress() {
        _events.value = Event(Navigation.ShowHideProgress)
    }

    sealed class Navigation {
        object ShowHideProgress : Navigation()
        data class ShowProgressChange(val progress : Int) : Navigation()
    }

    companion object {
        const val NameStep = 1
        const val BirthDateStep = 2
        const val GenderStep = 3
        const val OrientationStep = 4
        const val UsernameStep = 5
        const val DescriptionStep = 6
        const val InterestsStep = 7
        const val ActivitiesStep = 8
        const val PhotosStep = 9
    }
}