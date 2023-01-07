package com.org.datingapp.features.auth.signin

import androidx.lifecycle.Observer
import com.org.datingapp.AndroidTest
import com.org.datingapp.MainDispatcherRule
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.platform.Event
import com.org.datingapp.features.auth.Authenticator
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInViewModelTests : AndroidTest() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var signInViewModel : SignInViewModel
    private lateinit var signIn : SignIn

    @MockK
    private lateinit var authenticator: Authenticator

    @MockK
    private lateinit var eventObserver : Observer<Event<SignInViewModel.Navigation>?>

    @Before
    fun setup() {
        signIn = SignIn(authenticator)
        signInViewModel = SignInViewModel(signIn)
    }

    @Test
    fun `singInViewModel when signIn with valid credentials should update livedata with signInSuccess`()  {
        val params = SignIn.Params("admin@mail.com", "admin1234")
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInSuccess)) } answers { callOriginal() }
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Right(UseCase.None())
        signInViewModel.events.observeForever(eventObserver)
        runBlocking {
            signInViewModel.signIn(params.email, params.password)
        }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInSuccess)) }
    }

    @Test
    fun `singInViewModel when signIn with invalid credentials should update livedata with wrongCredentialsError`() {
        val params = SignIn.Params("admin@mail.com", "invalid.password")
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowWrongCredentialsError)) } answers { callOriginal() }
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Left(Authenticator.Failures.WrongCredentials())
        signInViewModel.events.observeForever(eventObserver)
        runBlocking {
            signInViewModel.signIn(params.email, params.password)
        }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowWrongCredentialsError)) }
    }

    @Test
    fun `singInViewModel when signIn with network unavailable should update livedata with networkError`() {
        val params = SignIn.Params("admin@mail.com", "admin1234")
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowNetworkError)) } answers { callOriginal() }
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Left(Failure.NetworkConnection)
        signInViewModel.events.observeForever(eventObserver)
        runBlocking {
            signInViewModel.signIn(params.email, params.password)
        }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowNetworkError)) }
    }

    @Test
    fun `singInViewModel when signIn with too many request should update livedata with tooManyRequestError`() {
        val params = SignIn.Params("admin@mail.com", "admin1234")
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowTooManyRequestsError)) } answers { callOriginal() }
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Left(Authenticator.Failures.TooManyRequest())
        signInViewModel.events.observeForever(eventObserver)
        runBlocking {
            signInViewModel.signIn(params.email, params.password)
        }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowTooManyRequestsError)) }
    }

    @Test
    fun `singInViewModel when signIn with serverError should update livedata with serverError`() {
        val params = SignIn.Params("admin@mail.com", "admin1234")
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowServerError)) } answers { callOriginal() }
        coEvery { authenticator.signIn(params.email, params.password) } returns Either.Left(Failure.ServerError)
        signInViewModel.events.observeForever(eventObserver)
        runBlocking {
            signInViewModel.signIn(params.email, params.password)
        }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowSignInStart)) }
        verify { eventObserver.onChanged(Event(SignInViewModel.Navigation.ShowServerError)) }
    }

}