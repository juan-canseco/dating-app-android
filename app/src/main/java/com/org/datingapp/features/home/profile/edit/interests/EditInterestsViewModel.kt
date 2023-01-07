package com.org.datingapp.features.home.profile.edit.interests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditInterestsViewModel
@Inject
constructor(private val editInterests: EditInterests) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun edit(interests : HashSet<Interest>) {
        _events.value = Event(Navigation.ShowEditInterestsStart)
        editInterests(EditInterests.Params(interests.toList()), viewModelScope) {
            val handleSuccess : (none : UseCase.None) -> Unit = {
                _events.value = Event(Navigation.ShowEditInterestsSuccess)
            }
            val handleFailures : (failure : Failure) -> Unit = {
                _events.value = Event(Navigation.ShowEditInterestsError)
            }
            it.fold(handleFailures, handleSuccess)
        }
    }

    sealed class Navigation {
        object ShowEditInterestsStart : Navigation()
        object ShowEditInterestsSuccess : Navigation()
        object ShowEditInterestsError : Navigation()
    }
}