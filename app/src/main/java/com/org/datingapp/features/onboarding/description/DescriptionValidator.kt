package com.org.datingapp.features.onboarding.description

import javax.inject.Inject

class DescriptionValidator @Inject constructor(){
    fun isValid(model: String) = model.length in 1..150
}