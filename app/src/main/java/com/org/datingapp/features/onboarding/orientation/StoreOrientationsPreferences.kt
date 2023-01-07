package com.org.datingapp.features.onboarding.orientation

import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class StoreOrientationsPreferences
@Inject
constructor(private val preferencesManager : SessionPreferences) : UseCase<UseCase.None, StoreOrientationsPreferences.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        preferencesManager.orientations = params.orientations
        return Either.Right(None())
    }

    data class Params(val orientations : HashSet<Orientation>)


}