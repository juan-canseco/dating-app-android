package com.org.datingapp.features.onboarding.orientation

import androidx.databinding.ObservableField

class OrientationListItemViewModel constructor(orientationId : Int , desc : String, selected : Boolean)  {
    val id = orientationId
    val name  = desc
    val checked = ObservableField(selected)
}


