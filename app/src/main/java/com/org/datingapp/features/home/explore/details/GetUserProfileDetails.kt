package com.org.datingapp.features.home.explore.details

import com.google.firebase.firestore.CollectionReference
import com.org.datingapp.R
import com.org.datingapp.core.di.UserCollection
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import com.org.datingapp.features.onboarding.birthdate.AgeCalculator
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetUserProfileDetails
@Inject
constructor(
    @UserCollection
    private val userCollection : CollectionReference,
    private val sessionPreferences: SessionPreferences,
    private val ageCalculator: AgeCalculator
) : UseCase<UserProfileDetailsView, GetUserProfileDetails.Params>() {

    private fun getColor(count : Int) : Int {
        return R.color.colorPrimary
    }

    private fun getInterestsInCommon(profileInterestsIds : List<String>) : List<Interest> {
        val userInterests = sessionPreferences.interests
        val set1 = userInterests.map { it.id }.toHashSet()
        val set2 = profileInterestsIds.toHashSet()
        val commonInterestsInCommon = mutableListOf<Interest>()
        userInterests.forEach { interest ->
            if (set1.contains(interest.id) && set2.contains(interest.id)) {
                commonInterestsInCommon.add(interest)
            }
        }
        return commonInterestsInCommon
    }

    private fun getActivitiesInCommon(profileActivitiesIds : List<String>) : List<Activity> {
        val userActivities = sessionPreferences.activities
        val set1 = userActivities.map { it.id }.toHashSet()
        val set2 = profileActivitiesIds.toHashSet()
        val commonActivitiesInCommon = mutableListOf<Activity>()
        userActivities.forEach { activity ->
            if (set1.contains(activity.id) && set2.contains(activity.id)) {
                commonActivitiesInCommon.add(activity)
            }
        }
        return commonActivitiesInCommon
    }

    private suspend fun getUserProfileDetails(userProfileId : String) : UserProfileDetailsView? {
        val userDetails = userCollection
            .document(userProfileId)
            .get()
            .await()
            .toObject(UserProfileDetails::class.java) ?: return null


        val fullNameWithAge = "${userDetails.name}, ${ageCalculator.getAge(userDetails.birthDate)}"
        val interestsInCommon = getInterestsInCommon(userDetails.interestsIds)
        val activitiesInCommon = getActivitiesInCommon(userDetails.activitiesIds)
        val numberOfInterestsInCommon = interestsInCommon.size + activitiesInCommon.size
        val matchPercentage : Int = numberOfInterestsInCommon * 100 / (userDetails.activities.size + userDetails.interests.size)

        val color = getColor(numberOfInterestsInCommon)
        return UserProfileDetailsView(
            userDetails.id,
            fullNameWithAge,
            userDetails.description,
            userDetails.username,
            userDetails.gender.name,
            userDetails.orientations.joinToString { it.name },
            userDetails.profilePictureUris,
            interestsInCommon,
            activitiesInCommon,
            userDetails.orientations,
            matchPercentage,
            color)
    }

    data class Params(val userProfileId : String)

    class Failures  {
        class UserNotFound : Failure.FeatureFailure()
    }

    override suspend fun run(params: Params): Either<Failure, UserProfileDetailsView> {
        return try {
            val userProfile = getUserProfileDetails(params.userProfileId)
                ?: return Either.Left(Failures.UserNotFound())

            Either.Right(userProfile)
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }
}