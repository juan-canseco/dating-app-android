package com.org.datingapp.features.home.explore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.org.datingapp.R
import com.org.datingapp.core.di.UserCollection
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.auth.Authenticator
import com.org.datingapp.features.onboarding.SessionPreferences
import com.org.datingapp.features.onboarding.birthdate.AgeCalculator
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetUserProfiles
@Inject
constructor(
    @UserCollection
    private val userCollection : CollectionReference,
    private val authenticator : Authenticator,
    private val sessionPreferences: SessionPreferences,
    private val ageCalculator: AgeCalculator) : UseCase<List<UserProfileView>, UseCase.None> () {

    private fun getUserProfilesQuery() : Query {
        return userCollection
            .whereNotEqualTo("id", authenticator.userId())
    }

    private fun getColor(count : Int) : Int {
        return R.color.colorPrimary
    }

    private suspend fun getUserProfiles(): List<UserProfileView> {

        val interestsIds = sessionPreferences.interests.map{it.id}
        val activitiesIds = sessionPreferences.activities.map{it.id}

        val profilesList = getUserProfilesQuery()
            .get()
            .await()
            .toObjects(UserProfile::class.java)

        val getNumberOfInterestsInCommon : (userInterestsIds : List<String>, profileInterestsIds : List<String>) -> Int = { userInterestsIds, profileInterestsIds ->
            val set1 = userInterestsIds.toHashSet()
            val set2 = profileInterestsIds.toHashSet()
            var count = 0
            userInterestsIds.forEach { interestId ->
                if (set1.contains(interestId) && set2.contains(interestId)) {
                    count++
                }
            }
            count
        }

        val getNumberOfActivitiesInCommon : (userActivitiesIds : List<String>, profileActivitiesIds : List<String>) -> Int = { userActivitiesIds, profileActivitiesIds ->
            val set1 = userActivitiesIds.toHashSet()
            val set2 = profileActivitiesIds.toHashSet()
            var count = 0
            userActivitiesIds.forEach { activityId ->
                if (set1.contains(activityId) && set2.contains(activityId)) {
                    count++
                }
            }
            count
        }


        return profilesList.map {

            val fullNameWithAge = "${it.name}, ${ageCalculator.getAge(it.birthDate)}"
            val numberOfInterestsInCommon = getNumberOfInterestsInCommon(interestsIds, it.interestsIds)
            val numberOfActivitiesInCommon = getNumberOfActivitiesInCommon(activitiesIds, it.activitiesIds)
            val totalNumberOfInterestsInCommon = numberOfInterestsInCommon + numberOfActivitiesInCommon
            val matchPercentage : Int = totalNumberOfInterestsInCommon * 100 / (it.activities.size + it.interests.size)
            val color = getColor(matchPercentage)
            UserProfileView(
                it.id,
                fullNameWithAge,
                it.description,
                it.profilePictureUris,
                matchPercentage,
                color)
        }.sortedByDescending {
            it.matchPercentage
        }
    }

    override suspend fun run(params: None): Either<Failure, List<UserProfileView>> {
        return try {
            val result = getUserProfiles()
            Either.Right(result)
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }
}