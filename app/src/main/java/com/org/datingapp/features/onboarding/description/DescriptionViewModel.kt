package com.org.datingapp.features.onboarding.description

import android.text.TextUtils
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.ObservableViewModel
import com.org.datingapp.BR
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(private val validator : DescriptionValidator): ObservableViewModel() {

    private val _events = MutableLiveData<Event<Navigation>>()
    val events get() = _events

    private var isDescriptionValid = false

    @get:Bindable
    var description = ""
        set (value) {
            field = value
            notifyPropertyChanged(BR.description)
            isDescriptionValid = validator.isValid(field)
            if (isDescriptionValid)
                _events.value = Event(Navigation.ShowValidDescription)
            else
                _events.value = Event(Navigation.ShowInvalidDescription)
            _events.value = Event(Navigation.ShowDescriptionClearErrors)
        }


    fun validate()  : Boolean {
        val isDescriptionEmpty = TextUtils.isEmpty(description)
        if (isDescriptionEmpty)
            _events.value = Event(Navigation.ShowRequiredDescriptionError)
        else if (!isDescriptionValid)
            _events.value = Event(Navigation.ShowDescriptionError)
        return isDescriptionValid
    }

    sealed class Navigation {
        object ShowValidDescription : Navigation()
        object ShowInvalidDescription : Navigation()
        object ShowRequiredDescriptionError : Navigation()
        object ShowDescriptionError : Navigation()
        object ShowDescriptionClearErrors : Navigation()
    }
}