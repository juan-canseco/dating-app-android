package com.org.datingapp.features.onboarding.photos

import android.net.Uri
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetLocalPhotosUris @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<List<Uri>, UseCase.None>(){
    override suspend fun run(params: None): Either<Failure, List<Uri>> {
        val photos = preferencesManager.photos
        val localUris = photos.map { Uri.parse(it.localUri) }
        return Either.Right(localUris)
    }
}