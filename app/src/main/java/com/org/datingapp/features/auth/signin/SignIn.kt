package com.org.datingapp.features.auth.signin


import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.interactor.UseCase.None
import com.org.datingapp.features.auth.Authenticator
import javax.inject.Inject

class SignIn @Inject constructor(private val authenticator : Authenticator): UseCase<None, SignIn.Params>() {
    override suspend fun run(params: Params): Either<Failure, None> = authenticator.signIn(params.email, params.password)
    data class Params(val email : String, val password : String)
}

