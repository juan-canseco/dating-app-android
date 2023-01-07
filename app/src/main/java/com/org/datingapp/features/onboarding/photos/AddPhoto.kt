package com.org.datingapp.features.onboarding.photos

import android.net.Uri
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject


class AddPhoto @Inject constructor(private val photoUploader: PhotoUploader,
                                   private val preferencesManager: SessionPreferences) : UseCase<UseCase.None, AddPhoto.Params>() {

    // Upload photos and store uris in preferences
    override suspend fun run(params: Params): Either<Failure, None> {
        return try {
            val photos = preferencesManager.photos
            if (photos.size == Constants.MaxNumberOfPhotos)
                Either.Left(Failures.MaxNumberOfPhotosExceeded())
            val localUri = params.photoUri.toString()
            val remoteUri = photoUploader.upload(params.photoUri)
            val photo = Photo(localUri, remoteUri)
            photos.add(photo)
            preferencesManager.photos = photos
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