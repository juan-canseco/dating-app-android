package com.org.datingapp.features.home.profile.edit.gender

import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.home.profile.ProfileManager
import javax.inject.Inject

class EditGender
@Inject
constructor(private val profileManager: ProfileManager) : UseCase<UseCase.None, EditGender.Params>(){
    override suspend fun run(params: Params): Either<Failure, None> {
        return try {
            profileManager.editGender(params.gender)
            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }
    data class Params(val gender : Gender)
}