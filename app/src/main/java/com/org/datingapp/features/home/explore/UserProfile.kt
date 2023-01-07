package com.org.datingapp.features.home.explore

import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.features.onboarding.UserLocation
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.domain.user.details.Interest

data class UserProfile
    (val id:  String = "",
     val name : String = "",
     val birthDate : BirthDate = BirthDate.empty(),
     val description : String? = null,
     val interests : List<Interest> = mutableListOf(),
     val interestsIds : List<String> = mutableListOf(),
     val activities : List<Activity> = mutableListOf(),
     val activitiesIds : List<String> = mutableListOf(),
     val profilePictureUris : List<String> = mutableListOf(),
     val location : UserLocation = UserLocation.empty()
) {}

