package com.org.datingapp.features.home.profile.edit.orientations

import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.home.profile.ProfileManager
import javax.inject.Inject

class EditOrientations
@Inject
constructor(private val profileManager: ProfileManager) : UseCase<UseCase.None, EditOrientations.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return try {
            profileManager.editOrientations(params.orientations)
            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }

    data class Params(val orientations : List<Orientation>)
}