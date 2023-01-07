package com.org.datingapp.features.onboarding.username

import android.text.TextUtils
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.org.datingapp.BR
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.platform.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(private val validator : UsernameValidator) : ObservableViewModel() {

    private val _events = MutableLiveData<Event<Navigation>>()
    val events get() = _events

    private var isUsernameValid = false
    @get:Bindable
    var username = ""
        set (value) {
            field = value
            notifyPropertyChanged(BR.username)
            isUsernameValid = validator.isValid(field)
            if (isUsernameValid)
                _events.value = Event(Navigation.ShowValidUsername)
            else
                _events.value = Event(Navigation.ShowInvalidUsername)
            _events.value = Event(Navigation.ShowClearUsernameError)
        }

    fun validate() : Boolean{
        val isUsernameEmpty = TextUtils.isEmpty(username)
        if (isUsernameEmpty)
            _events.value = Event(Navigation.ShowUsernameRequiredError)
        else if (!isUsernameValid)
            _events.value = Event(Navigation.ShowUsernameError)
        return isUsernameValid
    }

    sealed class Navigation {
        object ShowValidUsername : Navigation()
        object ShowInvalidUsername : Navigation()
        object ShowUsernameError : Navigation()
        object ShowUsernameRequiredError : Navigation()
        object ShowClearUsernameError : Navigation()
    }
}