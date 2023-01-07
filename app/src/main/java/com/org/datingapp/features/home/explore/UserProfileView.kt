package com.org.datingapp.features.home.explore


import androidx.annotation.ColorRes

data class UserProfileView (val id : String,
                            val fullNameWithAge : String,
                            val description : String?,
                            val profilePicturesUris: List<String>,
                            val matchPercentage : Int,
                            @ColorRes
                            val interestsColor : Int)
