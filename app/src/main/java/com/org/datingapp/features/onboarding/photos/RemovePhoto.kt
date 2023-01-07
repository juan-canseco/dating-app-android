package com.org.datingapp.features.onboarding.photos

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class RemovePhoto @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<UseCase.None, RemovePhoto.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        val photos = preferencesManager.photos

        if (params.photoPosition !in 0 until photos.size)
            Either.Left(Failures.InvalidPhotoPosition())

        photos.removeAt(params.photoPosition)

        preferencesManager.photos = photos

        return Either.Right(None())
    }

    data class Params(val photoPosition : Int)

    class Failures {
        class InvalidPhotoPosition : Failure.FeatureFailure()
    }
}