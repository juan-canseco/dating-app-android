package com.org.datingapp.features.onboarding


data class UserLocation(var latitude : Double? = 0.0,
                        var  longitude : Double? = 0.0) {

    constructor() : this(0.0, 0.0) {}

    companion object {
        fun empty() : UserLocation {
            return UserLocation()
        }
    }

}