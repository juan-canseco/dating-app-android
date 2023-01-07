package com.org.datingapp.features.onboarding.description

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class StoreDescriptionPreference @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<UseCase.None, StoreDescriptionPreference.Params>() {
    override suspend fun run(params: Params): Either<Failure, None> {
        preferencesManager.description = params.description
        return Either.Right(None())
    }
    data class Params(val description : String)
}