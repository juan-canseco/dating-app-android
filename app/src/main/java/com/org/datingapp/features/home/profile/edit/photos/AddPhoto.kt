package com.org.datingapp.features.home.profile.edit.photos

import android.net.Uri
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.home.profile.ProfileManager
import com.org.datingapp.features.onboarding.SessionPreferences
import com.org.datingapp.features.onboarding.photos.Constants
import com.org.datingapp.features.onboarding.photos.Photo
import com.org.datingapp.features.onboarding.photos.PhotoUploader
import javax.inject.Inject

class AddPhoto
@Inject
constructor(private val photoUploader: PhotoUploader,
            private val sessionPreferences: SessionPreferences,
            private val profileManager: ProfileManager)
    : UseCase<UseCase.None, AddPhoto.Params>(){

    override suspend fun run(params: Params): Either<Failure, None> {
        return try {

            val photos = sessionPreferences.photos
            if (photos.size == Constants.MaxNumberOfPhotos)
                Either.Left(Failures.MaxNumberOfPhotosExceeded())

            val localUri = params.photoUri.toString()
            val remoteUri = photoUploader.upload(params.photoUri)
            val photo = Photo(localUri, remoteUri)
            photos.add(photo)

            profileManager.editPhotos(photos)

            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }

    data class Params(val photoUri : Uri)

    class Failures {
        class MaxNumberOfPhotosExceeded : Failure.FeatureFailure()
    }

}