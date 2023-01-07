package com.org.datingapp.features.home.profile.edit.name

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.interactor.UseCase.None
import com.org.datingapp.features.home.profile.ProfileManager
import javax.inject.Inject

class EditName
@Inject
constructor(private val profileManager: ProfileManager): UseCase<None, EditName.Params>() {
    override suspend fun run(params: Params): Either<Failure, None> {
        return try {
            profileManager.editName(params.name)
            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }

    data class Params(val name : String)
}