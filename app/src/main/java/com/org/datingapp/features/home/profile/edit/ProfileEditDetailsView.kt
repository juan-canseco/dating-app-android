package com.org.datingapp.features.home.profile.edit

import android.net.Uri

data class ProfileEditDetailsView(
    val name: String,
    val gender : String,
    val orientation: String,
    val birthDate : String,
    val numberOfSelectedInterests : Int,
    val numberOfSelectedActivities : Int,
    val numberOfSelectedOrientations : Int,
    val profilePictures : List<Uri>)