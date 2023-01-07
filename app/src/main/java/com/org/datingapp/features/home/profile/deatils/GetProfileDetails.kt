package com.org.datingapp.features.home.profile.deatils

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import com.org.datingapp.features.onboarding.birthdate.AgeCalculator
import javax.inject.Inject

class GetProfileDetails
@Inject
constructor(private val sessionPreferences : SessionPreferences,
            private val ageCalculator: AgeCalculator) : UseCase<ProfileDetailsView, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, ProfileDetailsView> {

        val age = ageCalculator.getAge(sessionPreferences.birthDate)
        val fullNameWithAge = "${sessionPreferences.name}, $age"

        val profileDetails = ProfileDetailsView(
            fullNameWithAge,
            sessionPreferences.description,
            sessionPreferences.username,
            sessionPreferences.gender.name,
            sessionPreferences.orientations.joinToString { it.name },
            sessionPreferences.photos.map {it .localUri},
            sessionPreferences.interests.toList(),
            sessionPreferences.activities.toList())

        return Either.Right(profileDetails)
    }
}