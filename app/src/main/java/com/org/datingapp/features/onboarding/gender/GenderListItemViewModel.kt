package com.org.datingapp.features.onboarding.gender

import androidx.databinding.ObservableField

class GenderListItemViewModel constructor(genderId : Int , desc : String, selected : Boolean)  {
    val id = genderId
    val name  = desc
    val checked = ObservableField(selected)
}


