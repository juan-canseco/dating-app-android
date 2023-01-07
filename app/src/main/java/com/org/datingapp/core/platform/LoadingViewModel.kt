package com.org.datingapp.core.platform

import androidx.databinding.Bindable
import com.org.datingapp.BR

class LoadingViewModel : ObservableViewModel() {

    @get:Bindable
    var isLoading : Boolean = false
    set (value) {
        field = value
        notifyPropertyChanged(BR.loading)
    }

}