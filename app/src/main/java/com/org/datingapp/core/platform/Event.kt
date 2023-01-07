package com.org.datingapp.core.platform

data class Event<out T>(private val content : T) {
    private var hasBeenHandled = false
    fun getContentIfNotHandled() : T? =
        if (hasBeenHandled) null
        else {
        hasBeenHandled = true
        content
    }
}