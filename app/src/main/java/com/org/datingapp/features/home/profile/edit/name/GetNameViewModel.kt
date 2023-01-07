package com.org.datingapp.features.home.profile.edit.name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.interactor.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.name.GetNamePreference
import javax.inject.Inject

@HiltViewModel
class GetNameViewModel
@Inject
constructor(private val getNamePreference : GetNamePreference) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getName() {
        getNamePreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (name : String) -> Unit = { name ->
                _events.value = Event(Navigation.ShowGetName(name))
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowGetName(val name : String) : Navigation()
    }
}