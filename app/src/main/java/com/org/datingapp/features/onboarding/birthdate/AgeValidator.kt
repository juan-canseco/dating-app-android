package com.org.datingapp.features.onboarding.birthdate

import javax.inject.Inject

class AgeValidator @Inject constructor() {
     fun isValid(age: Int) = age > 17
}