package com.org.datingapp.features.onboarding.activities

import androidx.databinding.ObservableField

class ActivityListItemView constructor(interestId : String, desc : String, selected : Boolean)  {
    val id = interestId
    val description  = desc
    val isSelected = ObservableField(selected)
}
