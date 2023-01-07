package com.org.datingapp.features.onboarding.username

import java.util.regex.Pattern
import javax.inject.Inject

class UsernameValidator @Inject constructor() {

    // https://javadeveloperzone.com/java-8/java-regular-expression-for-username/
    fun isValid(model: String) : Boolean {
        val regex = "^([a-zA-Z])+([\\w]{4,})+$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(model)
        return matcher.find() && model.length in 5..30 && model[model.length - 1] != '_'
    }
}