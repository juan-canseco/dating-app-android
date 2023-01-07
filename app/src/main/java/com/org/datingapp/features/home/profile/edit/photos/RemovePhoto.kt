package com.org.datingapp.features.home.profile.edit.photos

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.home.profile.ProfileManager
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject


// Implement proper logic to remove photo
// Refactor this
class RemovePhoto
@Inject
constructor(private val preferencesManager: SessionPreferences,
            private val profileManager: ProfileManager) : UseCase<UseCase.None, RemovePhoto.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {

        val photos = preferencesManager.photos

        if (params.photoPosition !in 0 until photos.size)
            Either.Left(Failures.InvalidPhotoPosition())

        photos.removeAt(params.photoPosition)

        profileManager.editPhotos(photos)

        return Either.Right(None())
    }

    data class Params(val photoPosition : Int)

    class Failures {
        class InvalidPhotoPosition : Failure.FeatureFailure()
    }
}