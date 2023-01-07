package com.org.datingapp.core.domain.user

import com.google.firebase.firestore.ServerTimestamp
import com.org.datingapp.core.domain.user.details.*
import com.org.datingapp.features.onboarding.UserLocation
import java.util.*

data class User(
    var id : String = "",
    var name : String = "",
    var normalizedName : String = "",
    var username : String = "",
    var normalizedUsername : String = "",
    var birthDate : BirthDate = BirthDate.empty(),
    var gender : Gender = Gender.empty(),
    var orientations : List<Orientation> = mutableListOf(),
    var description : String? = null,
    var interests : List<Interest> = mutableListOf(),
    var activities : List<Activity> = mutableListOf(),
    var interestsIds : List<String> = mutableListOf(),
    var activitiesIds : List<String> = mutableListOf(),
    var orientationsIds : List<Int> = mutableListOf(),
    var profilePictureUris : List<String> = mutableListOf(),
    var location : UserLocation = UserLocation.empty(),
    @ServerTimestamp
    var timestamp : Date? = null,
    var lastConnection : Date? = null,
    var online : Boolean = false) {

    companion object {
        @JvmStatic
        fun create(id : String,
                   name : String,
                   username : String,
                   birthDate: BirthDate,
                   gender : Gender,
                   orientations : List<Orientation>,
                   description: String?,
                   interests : List<Interest>,
                   activities : List<Activity>,
                   photosUris : List<String>,
                   location : UserLocation) : User {

            return User(
                id,
                name,
                name.lowercase(),
                username,
                username.lowercase(),
                birthDate,
                gender,
                orientations,
                description,
                interests,
                activities,
                interests.map { it.id },
                activities.map {it.id},
                orientations.map { it.id },
                photosUris,
                location)

        }
    }

}

