package com.org.datingapp.features.onboarding.name

import java.util.regex.Pattern
import javax.inject.Inject

class NameValidator @Inject constructor() {
    fun isValid(name: String): Boolean {
        val regex = "^[\\p{L} .'-]+$"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(name)
        val match = matcher.find()
        return name.length in Constants.MinLength..Constants.MaxLength && match
    }
}
