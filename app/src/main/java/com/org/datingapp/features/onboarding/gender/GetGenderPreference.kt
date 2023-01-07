package com.org.datingapp.features.onboarding.gender

import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetGenderPreference @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<Gender, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, Gender> {
        val gender = preferencesManager.gender
        return Either.Right(gender)
    }
}