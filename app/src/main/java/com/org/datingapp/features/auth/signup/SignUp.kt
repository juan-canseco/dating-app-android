package com.org.datingapp.features.auth.signup

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.auth.Authenticator
import javax.inject.Inject

class SignUp @Inject constructor(private val authenticator: Authenticator) : UseCase<UseCase.None, SignUp.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> =
        authenticator.signUp(params.email, params.password)

    data class Params(val email : String, val password : String)

}