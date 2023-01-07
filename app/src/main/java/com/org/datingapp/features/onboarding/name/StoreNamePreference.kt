package com.org.datingapp.features.onboarding.name

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class StoreNamePreference @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<UseCase.None, StoreNamePreference.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        preferencesManager.name = params.name
        return Either.Right(None())
    }

    data class Params(val name : String)
}