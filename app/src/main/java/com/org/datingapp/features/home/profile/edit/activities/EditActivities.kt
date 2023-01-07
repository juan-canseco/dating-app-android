package com.org.datingapp.features.home.profile.edit.activities

import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.home.profile.ProfileManager
import javax.inject.Inject

class EditActivities
@Inject
constructor(private val profileManager: ProfileManager) : UseCase<UseCase.None, EditActivities.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return try {
            profileManager.editActivities(params.activities)
            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }

    data class Params(val activities : List<Activity>)
}