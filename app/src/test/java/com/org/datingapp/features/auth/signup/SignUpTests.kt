package com.org.datingapp.features.auth.signup

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
class SignUpTests : UnitTest() {

    private lateinit var signUp : SignUp

    @MockK
    private lateinit var authenticator : Authenticator

    @Before
    fun setup() {
        signUp = SignUp(authenticator)
    }

    @Test
    fun `signUp when user valid is valid returns none`() = runTest {
        val params = SignUp.Params("admin@mail.com", "admin1234")
        coEvery { authenticator.signUp(params.email, params.password) } returns Either.Right(UseCase.None())
        val result = signUp.run(params)
        result.isRight shouldBe true
    }

    @Test
    fun `signUp when email already exists returns emailCollision`() = runTest {
        val params = SignUp.Params("admin@mail.com", "admin1234")
        coEvery { authenticator.signUp(params.email, params.password) } returns Either.Left(Authenticator.Failures.EmailCollision())
        val result = signUp.run(params)
        result.isLeft shouldBe true
    }

    @Test
    fun `signUp when network connection is unavailable returns networkConnectionFailure`() = runTest {
        val params = SignUp.Params("admin@mail.com", "admin1234")
        coEvery { authenticator.signUp(params.email, params.password) } returns Either.Left(Failure.NetworkConnection)
        val result = signUp.run(params)
        result.isLeft shouldBe true
    }

    @Test
    fun `signUp when server error returns serverErrorFailure`() = runTest {
        val params = SignUp.Params("admin@mail.com", "admin1234")
        coEvery { authenticator.signUp(params.email, params.password) } returns Either.Left(Failure.ServerError)
        val result = signUp.run(params)
        result.isLeft shouldBe true
    }
}