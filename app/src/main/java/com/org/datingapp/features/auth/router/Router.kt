package com.org.datingapp.features.auth.router

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.org.datingapp.core.domain.user.User
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.auth.Authenticator
import com.org.datingapp.features.onboarding.SessionPreferences
import com.org.datingapp.features.onboarding.photos.Photo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class Router @Inject constructor(private val authenticator: Authenticator,
                                 private val sessionManager : SessionPreferences,
                                 private val firebaseStorage: FirebaseStorage,
                                 @ApplicationContext
                                 private val context : Context) : UseCase<UseCase.None, UseCase.None>() {


    private suspend fun downloadPhotos(uris : List<String>) : MutableList<Photo> {
        val photos = mutableListOf<Photo>()
        val storageReference = firebaseStorage.reference
        uris.forEach { remoteUri ->
            val imageReference = storageReference.storage.getReferenceFromUrl(remoteUri)

            val profileImagesFolder = File(context.filesDir, "profileImages")
            if (!profileImagesFolder.exists()) {
                profileImagesFolder.mkdirs()
            }

            val localFile = File(profileImagesFolder, "${UUID.randomUUID()}.jpg")
            val out = FileOutputStream(localFile)
            imageReference.getFile(localFile).await()

            out.flush()
            out.close()

            val localUri = Uri.fromFile(localFile)

            val photo = Photo(
                localUri.toString(),
                remoteUri)

            photos.add(photo)
        }
        return photos
    }


    private suspend fun storeProfileInCache(user : User){
        if (sessionManager.profileInCache)
            return

        sessionManager.name = user.name
        sessionManager.birthDate = user.birthDate
        sessionManager.gender = user.gender
        sessionManager.orientations = user.orientations.toHashSet()
        sessionManager.username = user.username
        sessionManager.description = user.description ?: ""
        sessionManager.interests = user.interests.toHashSet()
        sessionManager.activities = user.activities.toHashSet()
        sessionManager.location = user.location
        sessionManager.photos = downloadPhotos(user.profilePictureUris)
        sessionManager.profileInCache = true
    }


    override suspend fun run(params: None): Either<Failure, None> {
        delay(1500)
        return if (authenticator.userLoggedIn()) {
            if (authenticator.profileComplete()) {
                if (!sessionManager.profileInCache) {
                    val user = authenticator.getCurrentUser()
                    storeProfileInCache(user)
                }
                Either.Right(None())
            }
            else
                Either.Left(RouterFailure.UserProfileIncomplete())
        } else
            Either.Left(RouterFailure.UserNotLoggedIn())
    }

}
class RouterFailure {
    class UserNotLoggedIn() : Failure.FeatureFailure()
    class UserProfileIncomplete() : Failure.FeatureFailure()
}

