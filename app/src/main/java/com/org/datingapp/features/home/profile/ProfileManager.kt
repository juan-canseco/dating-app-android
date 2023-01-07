package com.org.datingapp.features.home.profile

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.org.datingapp.core.di.UserCollection
import com.org.datingapp.core.domain.user.details.*
import com.org.datingapp.features.auth.Authenticator
import com.org.datingapp.features.onboarding.SessionPreferences
import com.org.datingapp.features.onboarding.photos.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ProfileManager {

    suspend fun editName(name : String)
    suspend fun editGender(gender : Gender)
    suspend fun editBirthDate(birthDate: BirthDate)
    suspend fun editDescription(description : String)
    suspend fun editInterests(interests : List<Interest>)
    suspend fun editActivities(activities : List<Activity>)
    suspend fun editPhotos(photos : List<Photo>)
    suspend fun editOrientations(orientations : List<Orientation>)

    class Implementation
    @Inject constructor(private val authenticator: Authenticator,
                        private val sessionPreferences : SessionPreferences,
                        @UserCollection
                        private val userReference : CollectionReference) : ProfileManager {

        override suspend fun editName(name: String) {

            val updates = hashMapOf(
                "name" to name,
                "normalizedName" to name.lowercase(),
                "timestamp" to FieldValue.serverTimestamp())

            return withContext(Dispatchers.IO) {
                userReference
                    .document(authenticator.userId())
                    .update(updates)
                    .await()
                sessionPreferences.name = name
            }
        }

        override suspend fun editGender(gender: Gender) {
            return withContext(Dispatchers.IO) {

                val updates = hashMapOf(
                    "gender" to gender,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                userReference
                    .document(authenticator.userId())
                    .update(updates)
                    .await()

                sessionPreferences.gender = gender
            }
        }

        override suspend fun editBirthDate(birthDate: BirthDate) {
            return withContext(Dispatchers.IO) {

                val updates = hashMapOf(
                    "birthDate" to birthDate,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                userReference
                    .document(authenticator.userId())
                    .update(updates)
                    .await()

                sessionPreferences.birthDate = birthDate
            }
        }

        override suspend fun editDescription(description: String) {
            return withContext(Dispatchers.IO) {
                val updates = hashMapOf(
                    "description" to description,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                userReference
                    .document(authenticator.userId())
                    .update(updates)
                    .await()
                sessionPreferences.description = description
            }
        }

        override suspend fun editInterests(interests: List<Interest>) {

            val updates = hashMapOf(
                "interests" to interests.toMutableList(),
                "interestsIds" to interests.toMutableList().map {it.id},
                "timestamp" to FieldValue.serverTimestamp()
            )

            return withContext(Dispatchers.IO) {
                userReference
                    .document(authenticator.userId())
                    .update(updates)
                    .await()
                sessionPreferences.interests = interests.toHashSet()
            }
        }

        override suspend fun editActivities(activities: List<Activity>) {
            return withContext(Dispatchers.IO) {
                val updates = hashMapOf(
                    "activities" to activities.toMutableList(),
                    "activitiesIds" to activities.toMutableList().map {it.id},
                    "timestamp" to FieldValue.serverTimestamp()
                )

                userReference
                    .document(authenticator.userId())
                    .update(updates)
                    .await()
                sessionPreferences.activities = activities.toHashSet()
            }
        }

        override suspend fun editPhotos(photos: List<Photo>) {
            return withContext(Dispatchers.IO) {

                val profilePicturesUris = photos.map { it.remoteUri }

                val updates = hashMapOf(
                    "profilePictureUris" to profilePicturesUris,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                userReference
                    .document(authenticator.userId())
                    .update(updates)
                    .await()

                sessionPreferences.photos = photos.toMutableList()
            }
        }

        override suspend fun editOrientations(orientations: List<Orientation>) {
            return withContext(Dispatchers.IO) {
                val updates = hashMapOf(
                    "orientations" to orientations.toMutableList(),
                    "orientationsIds" to orientations.toMutableList().map {it.id},
                    "timestamp" to FieldValue.serverTimestamp()
                )

                userReference
                    .document(authenticator.userId())
                    .update(updates)
                    .await()
                sessionPreferences.orientations = orientations.toHashSet()
            }
        }

    }
}