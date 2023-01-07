package com.org.datingapp.features.onboarding.interests

import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class StoreInterestsPreferences
@Inject
constructor(private val preferencesManager: SessionPreferences) :
    UseCase<UseCase.None, StoreInterestsPreferences.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        preferencesManager.interests = params.interests
        return Either.Right(None())
    }

    data class Params(val interests : HashSet<Interest>)
}