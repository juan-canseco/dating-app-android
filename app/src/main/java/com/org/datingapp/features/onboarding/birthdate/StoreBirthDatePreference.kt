package com.org.datingapp.features.onboarding.birthdate

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class StoreBirthDatePreference  @Inject constructor(private val preferencesManager : SessionPreferences):
    UseCase<UseCase.None, StoreBirthDatePreference.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        preferencesManager.birthDate = params.birthDate
        return Either.Right(None())
    }

    data class Params(val birthDate : BirthDate)
}