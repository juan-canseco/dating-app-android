package com.org.datingapp.features.onboarding.interests

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
class InterestViewModel
@Inject
constructor(private val getAllInterests: GetAllInterests) : ViewModel() {

    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events


    fun getInterests() {
        getAllInterests(UseCase.None(), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (interests : List<Interest>) -> Unit = { interests ->
                val interestsVm =
                    interests.map { interest ->
                        InterestListItemView(
                            interest.id,
                            interest.name,
                            false)
                    }
                _events.value = Event(Navigation.ShowGetAll(interestsVm))
            }
            it.fold(handleFailure, handleSuccess)
        }
    }


    private var _interests =  hashSetOf<Interest>()

    var interests
    get() = _interests
    set (value) {
        _interests.clear()
        _interests.addAll(value)
    }

    val isValid get() = _interests.size >= Constants.MinNumberOfInterests

    val count get() = _interests.size

    fun addInterest(interest : Interest) {
        if (_interests.contains(interest))
            return
        _interests.add(interest)
    }

    fun removeInterest(interest : Interest) {
        if (!_interests.contains(interest))
            return
        _interests.remove(interest)
    }


    sealed class Navigation {
        data class ShowGetAll(val interests : List<InterestListItemView>) : Navigation()
    }
}