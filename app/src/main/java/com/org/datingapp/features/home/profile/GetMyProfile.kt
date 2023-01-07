package com.org.datingapp.features.home.profile

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import com.org.datingapp.features.onboarding.birthdate.AgeCalculator
import javax.inject.Inject

class GetMyProfile
@Inject
constructor(private val sessionPreferences : SessionPreferences,
            private val ageCalculator: AgeCalculator) : UseCase<ProfileView, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, ProfileView> {

        val profileView = ProfileView(
            sessionPreferences.name,
            ageCalculator.getAge(sessionPreferences.birthDate),
            sessionPreferences.photos[0].localUri)

        return Either.Right(profileView)

    }

}