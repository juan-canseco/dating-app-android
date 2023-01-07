package com.org.datingapp.features.auth.router

import com.org.datingapp.UnitTest
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
class RouterTests : UnitTest() {

    private lateinit var router:  Router

    @MockK
    private lateinit var authenticator : Authenticator

    @Before
    fun setup() {
        router = Router(authenticator)
    }

    @Test
    fun `router when user logged and user profile is complete returns none`() = runTest {
        coEvery { authenticator.userLoggedIn() } returns true
        coEvery { authenticator.profileComplete() } returns true
        val result = router.run(UseCase.None())
        result.isRight shouldBe true
    }

    @Test
    fun `router when user is not logged in returns userNotLoggedInFailure`() = runTest {
        coEvery { authenticator.userLoggedIn() } returns false
        val result = router.run(UseCase.None())
        result.isLeft shouldBe true
    }


    @Test
    fun `router when user logged in and profile is incomplete returns userProfileIncompleteFailure`() = runTest {
        coEvery { authenticator.userLoggedIn() } returns true
        coEvery { authenticator.profileComplete() } returns false
        val result = router.run(UseCase.None())
        result.isLeft shouldBe true
    }

}