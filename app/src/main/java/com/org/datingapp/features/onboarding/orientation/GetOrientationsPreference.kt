package com.org.datingapp.features.onboarding.orientation

import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetOrientationsPreference
@Inject
constructor(private val sessionPreferences: SessionPreferences) : UseCase<HashSet<Orientation>, UseCase.None>(){
    override suspend fun run(params: None): Either<Failure, HashSet<Orientation>> {
        val orientations = sessionPreferences.orientations
        return Either.Right(orientations)
    }
}