package com.org.datingapp.features.home.profile.edit.birthdate

import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.home.profile.ProfileManager
import javax.inject.Inject

class EditBirthDate
@Inject
constructor(private val profileManager: ProfileManager) : UseCase<UseCase.None, EditBirthDate.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return try {
            profileManager.editBirthDate(params.birthDate)
            Either.Right(None())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failure.ServerError)
        }
    }
    data class Params(val birthDate: BirthDate)
}