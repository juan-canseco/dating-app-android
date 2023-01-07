package com.org.datingapp.features.onboarding.gender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GendersViewModel
@Inject
constructor(private val getAllGenders: GetAllGenders) : ViewModel() {


    private var _events = MutableLiveData<Event<Navigation>?>()
    val events : LiveData<Event<Navigation>?> get() = _events

    fun getGenders() {
        getAllGenders(UseCase.None(), viewModelScope) {
            val handleFailure : (failure : Failure) -> Unit = {}
            val handleSuccess : (genders : List<Gender>) -> Unit = { genders ->
                val gendersVM =
                    genders.map { gender ->
                        GenderListItemViewModel(
                            gender.id,
                            gender.name,
                            false
                        )
                    }
                _events.value = Event(Navigation.ShowGetAll(gendersVM))
            }
            it.fold(handleFailure, handleSuccess)
        }
    }

    var selectedGender : Gender? = null

    val isValid get() = selectedGender  != null

    sealed class Navigation {
        data class ShowGetAll(val genders : List<GenderListItemViewModel>) : Navigation()
    }

}