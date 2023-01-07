package com.org.datingapp.features.onboarding.description

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetDescriptionPreference @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<String, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, String> {
        val description = preferencesManager.description ?: ""
        return Either.Right(description)
    }
}