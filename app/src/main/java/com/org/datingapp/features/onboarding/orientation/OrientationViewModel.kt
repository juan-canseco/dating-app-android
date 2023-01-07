package com.org.datingapp.features.onboarding.orientation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class  OrientationViewModel
@Inject
constructor(private val getAllOrientations: GetAllOrientations) : ViewModel(){

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getOrientations() {
        getAllOrientations(UseCase.None(), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (orientations : List<Orientation>) -> Unit = { orientations ->
                val orientationsVm = orientations.map { orientation ->
                    OrientationListItemViewModel(
                        orientation.id,
                        orientation.name,
                        false)
                }
                _events.value = Event(Navigation.ShowGetAll(orientationsVm))
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    private var _orientations =  hashSetOf<Orientation>()

    var orientations
        get() = _orientations
        set (value) {
            _orientations.clear()
            _orientations.addAll(value)
        }

    val isValid get() = _orientations.size >= Constants.MinNumberOfOrientations && _orientations.size <= Constants.MaxNumberOfOrientations

    val count get() = _orientations.size

    fun addOrientation(orientation : Orientation) {
        if (_orientations.contains(orientation))
            return
        _orientations.add(orientation)
    }

    fun removeOrientation(orientation : Orientation) {
        if (!_orientations.contains(orientation))
            return
        _orientations.remove(orientation)
    }

    sealed class Navigation {
        data class ShowGetAll(val orientations : List<OrientationListItemViewModel>) : Navigation()
    }
}