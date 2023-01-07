package com.org.datingapp.features.onboarding

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.org.datingapp.core.di.SessionSharedPreferences
import com.org.datingapp.core.domain.user.details.*
import com.org.datingapp.features.onboarding.photos.Photo
import javax.inject.Inject

interface SessionPreferences {

    var name : String
    var birthDate : BirthDate
    var gender: Gender
    var username : String
    var description : String
    var interests : HashSet<Interest>
    var activities : HashSet<Activity>
    var orientations : HashSet<Orientation>
    var photos : MutableList<Photo>
    var location : UserLocation
    var profileInCache : Boolean

    fun clearPreferences()

    class Implementation @Inject constructor(@SessionSharedPreferences
                                                           private val sharedPreferences : SharedPreferences,
                                                           private val gson : Gson) : SessionPreferences {

        override var name : String
            get() = sharedPreferences.getString(fullNameKey, null) ?: ""
            set (value) {
                with (sharedPreferences.edit()) {
                    putString(fullNameKey, value)
                    apply()
                }
            }


        override var profileInCache: Boolean
            get() = sharedPreferences.getBoolean(profileInCacheKey, false )
            set(value) {
                with (sharedPreferences.edit()) {
                    putBoolean(profileInCacheKey, value)
                    apply()
                }
            }

        override var birthDate : BirthDate
            get() {
                val json = sharedPreferences.getString(birthDateKey, null) ?: return BirthDate.empty()
                return gson.fromJson(json, BirthDate::class.java)
            }
            set (value) {
                with(sharedPreferences.edit()) {
                    putString(birthDateKey, gson.toJson(value))
                    apply()
                }
            }

        // Continue implementing get the las know location tomorrow
        override var location: UserLocation
            get()  {
                val json = sharedPreferences.getString(userLocationKey, null) ?: return UserLocation()
                return gson.fromJson(json, UserLocation::class.java)
            }
            set(value) {
                with (sharedPreferences.edit()) {
                    putString(userLocationKey, gson.toJson(value))
                    apply()
                }
            }


        override var gender: Gender
            get() {
                val json = sharedPreferences.getString(genderKey, null) ?: return Gender()
                return gson.fromJson(json, Gender::class.java)
            }
            set(value) {
                with(sharedPreferences.edit()) {
                    putString(genderKey, gson.toJson(value))
                    apply()
                }
            }

        override var username : String
            get() = sharedPreferences.getString(usernameKey, null) ?: ""
            set (value) {
                with (sharedPreferences.edit()) {
                    putString(usernameKey, value)
                    apply()
                }
            }

        override var description : String
            get() = sharedPreferences.getString(descriptionKey, null) ?: ""
            set(value) {
                with(sharedPreferences.edit()) {
                    putString(descriptionKey, value)
                    apply()
                }
            }

        override var interests : HashSet<Interest>
            get() {
                val json = sharedPreferences.getString(interestsKey, null) ?: return hashSetOf()
                val itemType = object : TypeToken<HashSet<Interest>>() {}.type
                return gson.fromJson(json, itemType)
            }
            set (value) {
                with (sharedPreferences.edit()) {
                    putString(interestsKey, gson.toJson(value))
                    apply()
                }
            }

        override var activities : HashSet<Activity>
            get() {
                val json = sharedPreferences.getString(activitiesKey, null) ?: return hashSetOf()
                val itemType = object : TypeToken<HashSet<Activity>>() {}.type
                return gson.fromJson(json, itemType)
            }
            set (value) {
                with (sharedPreferences.edit()) {
                    putString(activitiesKey, gson.toJson(value))
                    apply()
                }
            }


        override var orientations: HashSet<Orientation>
            get() {
                val json = sharedPreferences.getString(orientationsKey, null) ?: return hashSetOf()
                val itemType = object : TypeToken<HashSet<Orientation>>() {}.type
                return gson.fromJson(json, itemType)
            }
            set(value) {
                with (sharedPreferences.edit()) {
                    putString(orientationsKey, gson.toJson(value))
                    apply()
                }
            }


        override var photos : MutableList<Photo>
            get() {
                val json = sharedPreferences.getString(photosKey, null) ?: return mutableListOf()
                val itemType = object : TypeToken<MutableList<Photo>>() {}.type
                return gson.fromJson(json, itemType)
            }
            set (value) {
                with(sharedPreferences.edit()) {
                    putString(photosKey, gson.toJson(value))
                    apply()
                }
            }

        override fun clearPreferences() {
            with (sharedPreferences.edit()) {
                clear()
                apply()
            }
        }

        companion object {
            const val fullNameKey = "fullName-key"
            const val birthDateKey = "birthDate-key"
            const val genderKey = "gender-key"
            const val usernameKey = "username-key"
            const val photosKey = "photos-key"
            const val descriptionKey = "description-key"
            const val interestsKey = "interests-key"
            const val activitiesKey = "activities-key"
            const val orientationsKey = "orientations-key"
            const val userLocationKey = "user-location-key"
            const val profileInCacheKey = "profile-in-cache-key"
        }
    }
}

