package com.org.datingapp.features.home.profile.edit


import android.net.Uri
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.interactor.UseCase.None
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetProfileEditDetails
@Inject
constructor(private val sessionManager : SessionPreferences) : UseCase<ProfileEditDetailsView, None>(){
    override suspend fun run(params: None): Either<Failure, ProfileEditDetailsView>  {

        val birthDate = sessionManager.birthDate
        val numberOfSelectedInterests = sessionManager.interests.size
        val numberOfSelectedActivities = sessionManager.activities.size
        val numberOfSelectedOrientations = sessionManager.orientations.size

        val birthDateMessage = "${birthDate.day}.${birthDate.month}.${birthDate.year}"
        val orientations = sessionManager.orientations.map{it.name}
        val orientationMessage = orientations.joinToString()
        val profileEditDetails = ProfileEditDetailsView(
            sessionManager.name,
            sessionManager.gender.name,
            orientationMessage,
            birthDateMessage,
            numberOfSelectedInterests,
            numberOfSelectedActivities,
            numberOfSelectedOrientations,
            sessionManager.photos.map { Uri.parse(it.localUri)})

        return Either.Right(profileEditDetails)
    }
}