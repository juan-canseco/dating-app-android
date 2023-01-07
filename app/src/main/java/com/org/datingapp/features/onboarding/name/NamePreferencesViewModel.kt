package com.org.datingapp.features.onboarding.name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NamePreferencesViewModel @Inject constructor(private val getNamePreference: GetNamePreference,
                                                   private val storeNamePreference : StoreNamePreference) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events

    fun storeName(name : String) {
        storeNamePreference(StoreNamePreference.Params(name), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowStoreNamePreference)
            }
            it.fold({}, handleSuccess)
        }
    }

    fun getName() {
        getNamePreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (name : String) -> Unit = { name ->
                _events.value = Event(Navigation.ShowGetNamePreference(name))
            }
            it.fold({}, handleSuccess)
        }
    }


    sealed class Navigation {
        data class ShowGetNamePreference(val name : String) : Navigation()
        object ShowStoreNamePreference : Navigation()
    }
}