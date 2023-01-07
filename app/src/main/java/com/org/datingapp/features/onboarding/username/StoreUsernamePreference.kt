package com.org.datingapp.features.onboarding.username

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class StoreUsernamePreference @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<UseCase.None, StoreUsernamePreference.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        preferencesManager.username = params.username
        return Either.Right(None())
    }

    data class Params(val username : String)
}