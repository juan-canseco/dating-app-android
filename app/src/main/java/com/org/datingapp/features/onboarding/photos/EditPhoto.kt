package com.org.datingapp.features.onboarding.photos

import android.net.Uri
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class EditPhoto @Inject constructor(private val photoUploader: PhotoUploader, private val preferencesManager: SessionPreferences) : UseCase<UseCase.None, EditPhoto.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return try {

            val photos = preferencesManager.photos

            if (params.photoPosition !in 0 until photos.size)
                Either.Left(Failures.InvalidPhotoPosition())


            val localUri = params.photoUri.toString()
            val oldRemoteUri = Uri.parse(
                photos[params.photoPosition].remoteUri
            )
            val remoteUri = photoUploader.upload(params.photoUri, oldRemoteUri)

            val photo = Photo(localUri, remoteUri)
            photos[params.photoPosition] = photo
            preferencesManager.photos = photos

            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }
    class Failures {
        class InvalidPhotoPosition : Failure.FeatureFailure()
    }
    data class Params(val photoUri : Uri, val photoPosition : Int)
}