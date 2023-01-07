package com.org.datingapp.features.onboarding.description

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DescriptionPreferencesViewModel @Inject constructor(private val getDescriptionPreference: GetDescriptionPreference, private val storeDescriptionPreference: StoreDescriptionPreference) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events get() = _events

    fun storeDescription(description : String) {
        storeDescriptionPreference(StoreDescriptionPreference.Params(description), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowStoreDescription)
            }
            it.fold({}, handleSuccess)
        }
    }

    fun getDescription() {
        getDescriptionPreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (description : String) -> Unit = { description ->
                _events.value = Event(Navigation.ShowGetDescription(description))
            }
            it.fold({}, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowStoreDescription : Navigation();
        data class ShowGetDescription(val description : String) : Navigation()
    }

}