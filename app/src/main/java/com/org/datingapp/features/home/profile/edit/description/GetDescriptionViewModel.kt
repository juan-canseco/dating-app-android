package com.org.datingapp.features.home.profile.edit.description

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.onboarding.description.GetDescriptionPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetDescriptionViewModel
@Inject
constructor(private val getDescriptionPreference: GetDescriptionPreference): ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events



    fun getDescription() {
        getDescriptionPreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (name : String) -> Unit = { description ->
                _events.value = Event(Navigation.ShowGetDescription(description))
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        data class ShowGetDescription(val description : String) : Navigation()
    }

}