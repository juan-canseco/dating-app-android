package com.org.datingapp.features.onboarding.username

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetUsernamePreference @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<String, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, String> {
        val username = preferencesManager.username ?: ""
        return Either.Right(username)
    }
}