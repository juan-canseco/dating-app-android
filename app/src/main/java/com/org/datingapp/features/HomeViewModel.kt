package com.org.datingapp.features

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private var _navigationVisibility = MutableLiveData<Boolean>()
    val navigationVisibility get() = _navigationVisibility

    fun changeNavigationVisibility(value : Boolean) {
        _navigationVisibility.value = value
    }

}