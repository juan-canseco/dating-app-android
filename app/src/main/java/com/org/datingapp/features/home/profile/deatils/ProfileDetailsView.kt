package com.org.datingapp.features.home.profile.deatils

import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.domain.user.details.Interest

data class ProfileDetailsView(
    val fullNameWithAge : String,
    val description : String,
    val username : String,
    val gender : String,
    val orientation : String,
    val profilePicturesUris : List<String>,
    val interests : List<Interest>,
    val activities : List<Activity>,
)