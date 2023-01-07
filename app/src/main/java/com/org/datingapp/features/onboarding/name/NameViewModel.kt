package com.org.datingapp.features.onboarding.name

import android.text.TextUtils
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.ObservableViewModel
import javax.inject.Inject
import com.org.datingapp.BR
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class NameViewModel @Inject constructor(private val validator : NameValidator): ObservableViewModel() {

    private var _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events


    private var isFullNameValid = false

    @get:Bindable
    var fullName = ""
        set (value) {
                field = value
                notifyPropertyChanged(BR.fullName)
                isFullNameValid = validator.isValid(field)
                if (isFullNameValid)
                    _events.value = Event(Navigation.ShowFullNameValid)
                else
                    _events.value = Event(Navigation.ShowFullNameInvalid)
                _events.value = Event(Navigation.ShowFullNameClearErrors)
        }

    fun validate() : Boolean {
        val isFullNameEmpty = TextUtils.isEmpty(fullName)
        if (isFullNameEmpty)
            _events.value = Event(Navigation.ShowFullNameRequired)
        else if (!isFullNameValid)
            _events.value = Event(Navigation.ShowFullNameError)
        return isFullNameValid
    }

    sealed class Navigation  {
        object ShowFullNameValid : Navigation()
        object ShowFullNameInvalid : Navigation()
        object ShowFullNameError : Navigation()
        object ShowFullNameRequired : Navigation()
        object ShowFullNameClearErrors : Navigation()
    }
}