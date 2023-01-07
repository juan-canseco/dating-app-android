package com.org.datingapp.features.onboarding.interests

import androidx.databinding.ObservableField


class InterestListItemView constructor(interestId : String, desc : String, selected : Boolean)  {
    val id = interestId
    val description  = desc
    val isSelected = ObservableField(selected)
}


