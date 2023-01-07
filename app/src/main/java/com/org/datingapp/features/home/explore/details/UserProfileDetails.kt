package com.org.datingapp.features.home.explore.details

import com.org.datingapp.core.domain.user.details.*
import com.org.datingapp.features.onboarding.UserLocation

data class UserProfileDetails
    (val id:  String = "",
     val name : String = "",
     val username : String = "",
     val birthDate : BirthDate = BirthDate.empty(),
     val gender : Gender = Gender.empty(),
     val description : String? = null,
     val interests : List<Interest> = mutableListOf(),
     val interestsIds : List<String> = mutableListOf(),
     val activities : List<Activity> = mutableListOf(),
     val activitiesIds : List<String> = mutableListOf(),
     val orientations  : List<Orientation> = mutableListOf(),
     val profilePictureUris : List<String> = mutableListOf(),
     val location : UserLocation = UserLocation.empty()
) {}
