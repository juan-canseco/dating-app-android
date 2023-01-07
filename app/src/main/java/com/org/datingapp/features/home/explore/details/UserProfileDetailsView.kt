package com.org.datingapp.features.home.explore.details

import androidx.annotation.ColorRes
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.domain.user.details.Orientation

data class UserProfileDetailsView (
    val id : String,
    val fullNameWithAge : String,
    val description : String?,
    val username : String,
    val gender : String,
    val orientation : String,
    val profilePicturesUris: List<String>,
    val interests : List<Interest>,
    val activities : List<Activity>,
    val orientations : List<Orientation>,
    val matchPercentage : Int,
    @ColorRes val interestsColor : Int)