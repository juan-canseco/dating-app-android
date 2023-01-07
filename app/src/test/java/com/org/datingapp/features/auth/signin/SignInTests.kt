package com.org.datingapp.features.auth.signin

import com.org.datingapp.UnitTest
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.auth.Authenticator
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInTests : UnitTest() {

    private lateinit var signIn : SignIn

    @MockK
    private lateinit var authenticator : Authenticator

    @Before
    fun setup() {
        signIn = SignIn(authenticator)
    }

    @Test
    fun `signIn when user credentials are valid returns none`() = runTest {
        val params = SignIn.Params("admin@emai.com", "admin1234")
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Right(UseCase.None())
        val result = signIn.run(params)
        result.isRight shouldBe true
    }

    @Test
    fun `signIn when user credentials are invalid returns wrongCredentialsFailure`() = runTest {
        val params = SignIn.Params("none.existent.user@mail.com", "none.existent.password")
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Left(Authenticator.Failures.WrongCredentials())
        val result = signIn.run(params)
        result.isLeft shouldBe true
    }

    @Test
    fun `signIn when network connection is unavailable returns networkConnectionFailure`() = runTest {
        val params = SignIn.Params("admin@emai.com", "admin1234")
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Left(Failure.NetworkConnection)
        val result = signIn.run(params)
        result.isLeft shouldBe true
    }

    @Test
    fun `singIn when server error returns serverErrorFailure`() = runTest {
        val params = SignIn.Params("admin@emai.com", "admin1234")
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Left(Failure.ServerError)
        val result = signIn.run(params)
        result.isLeft shouldBe true
    }

    @Test
    fun `singIn when too many requests returns tooManyRequestFailure`() = runTest {
        val params = SignIn.Params("admin@emai.com", "admin1234")
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Left(Authenticator.Failures.TooManyRequest())
        val result = signIn.run(params)
        result.isLeft shouldBe true
    }

}