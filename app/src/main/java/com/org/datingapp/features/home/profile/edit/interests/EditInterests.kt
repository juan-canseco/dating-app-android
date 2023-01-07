package com.org.datingapp.features.home.profile.edit.interests

import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.home.profile.ProfileManager
import javax.inject.Inject

class EditInterests
@Inject
constructor(private val profileManager: ProfileManager) : UseCase<UseCase.None, EditInterests.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return try {
            profileManager.editInterests(params.interests)
            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }

    data class Params(val interests : List<Interest>)
}