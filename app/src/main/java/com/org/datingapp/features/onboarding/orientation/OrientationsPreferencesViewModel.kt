package com.org.datingapp.features.onboarding.orientation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrientationsPreferencesViewModel
@Inject
constructor(
    private val getOrientationsPreference: GetOrientationsPreference,
    private val storeOrientationsPreferences: StoreOrientationsPreferences) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events


    fun getOrientations() {
        getOrientationsPreference(UseCase.None(), viewModelScope) {
            val handleSuccess : (orientations : HashSet<Orientation>) -> Unit = { orientations ->
                _events.value = Event(Navigation.ShowGetOrientations(orientations))
            }
            it.fold({}, handleSuccess)
        }
    }

    fun storeOrientations(orientations : HashSet<Orientation>) {
        storeOrientationsPreferences(StoreOrientationsPreferences.Params(orientations), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowStoreOrientations)
            }
            it.fold({}, handleSuccess)
        }
    }


    sealed class Navigation {
        object ShowStoreOrientations : Navigation()
        data class ShowGetOrientations(val orientations : HashSet<Orientation>) : Navigation()
    }
}