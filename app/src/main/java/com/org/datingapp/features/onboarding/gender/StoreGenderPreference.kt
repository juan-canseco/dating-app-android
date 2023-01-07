package com.org.datingapp.features.onboarding.gender

import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class StoreGenderPreference @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<UseCase.None, StoreGenderPreference.Params>() {
    override suspend fun run(params: Params): Either<Failure, None> {
        preferencesManager.gender = params.gender
        return Either.Right(None())
    }
    data class Params(val gender : Gender)
}