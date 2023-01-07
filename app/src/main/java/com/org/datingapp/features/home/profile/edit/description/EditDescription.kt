package com.org.datingapp.features.home.profile.edit.description

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.home.profile.ProfileManager
import javax.inject.Inject

class EditDescription
@Inject
constructor(private val profileManager: ProfileManager) : UseCase<UseCase.None, EditDescription.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return try {
            profileManager.editDescription(params.description)
            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }

    data class Params(val description : String)
}