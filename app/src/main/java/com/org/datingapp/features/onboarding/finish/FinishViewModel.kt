package com.org.datingapp.features.onboarding.finish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinishViewModel @Inject constructor(private val finish : Finish) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>>()
    val events : LiveData<Event<Navigation>> get() = _events

    private var _isSuccessful = false

    fun finish() {
        finish(UseCase.None(), viewModelScope) {
            val handleFailure : (Failure) -> Unit = { failure ->
                when (failure) {
                    is Failure.NetworkConnection -> {
                        _events.value = Event(Navigation.ShowNetworkError)
                    }
                    is Failure.ServerError -> {
                        _events.value = Event(Navigation.ShowServerError)
                    }
                    else -> {}
                }
            }
            val handleSuccess : (UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowFinishSuccess)
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowNetworkError : Navigation()
        object ShowServerError : Navigation()
        object ShowFinishSuccess : Navigation()
    }
}