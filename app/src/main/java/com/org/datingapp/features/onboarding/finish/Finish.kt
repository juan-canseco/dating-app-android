package com.org.datingapp.features.onboarding.finish

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.auth.Authenticator
import com.org.datingapp.core.domain.user.User
import com.org.datingapp.features.onboarding.SessionPreferences
import kotlinx.coroutines.delay
import javax.inject.Inject

class Finish @Inject constructor(private val preferencesManager: SessionPreferences,
                                 private val authenticator: Authenticator) :
    UseCase<UseCase.None, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, None> {
        delay(2000L)
        // Refactor this
        val user = User.create(
            authenticator.userId(),
            preferencesManager.name,
            preferencesManager.username,
            preferencesManager.birthDate,
            preferencesManager.gender,
            preferencesManager.orientations.toList(),
            preferencesManager.description,
            preferencesManager.interests.toList(),
            preferencesManager.activities.toList(),
            preferencesManager.photos.map { p -> p.remoteUri },
            preferencesManager.location)
        val result = authenticator.completeProfile(user)
        preferencesManager.profileInCache = true
        return result
    }
}