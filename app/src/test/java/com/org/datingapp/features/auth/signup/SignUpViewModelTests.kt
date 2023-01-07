package com.org.datingapp.features.auth.signup

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
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SignUpViewModelTests : AndroidTest() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var signUp : SignUp

    @MockK
    private lateinit var authenticator: Authenticator

    @MockK
    private lateinit var eventObserver: Observer<Event<SignUpViewModel.Navigation>?>

    @Before
    fun setup() {
        signUp = SignUp(authenticator)
        signUpViewModel = SignUpViewModel(signUp)
    }

    @Test
    fun `singUpViewModel when signUp with valid user should update livedata with signUpSuccess`() = runTest {
        val params = SignUp.Params("admin@mail.com", "admin1234")
        every { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpSuccess)) } answers { callOriginal() }
        coEvery { authenticator.signUp(params.email, params.password) } returns Either.Right(UseCase.None())
        signUpViewModel.events.observeForever(eventObserver)
        signUpViewModel.signUp(params.email, params.password)
        verify { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpStart)) }
        verify { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpSuccess)) }
    }

    @Test
    fun `singUpViewModel when signUp with existent email should update livedata with emailAlreadyExistsError`() = runTest {
        val params = SignUp.Params("admin@mail.com", "admin1234")
        every { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowEmailAlreadyExistsError)) } answers { callOriginal() }
        coEvery { authenticator.signUp(params.email, params.password) } returns Either.Left(Authenticator.Failures.EmailCollision())
        signUpViewModel.events.observeForever(eventObserver)
        signUpViewModel.signUp(params.email, params.password)
        verify { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpStart)) }
        verify { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowEmailAlreadyExistsError)) }
    }

    @Test
    fun `singUpViewModel when signUp with network unavailable should update livedata with networkError`() = runTest {
        val params = SignUp.Params("admin@mail.com", "admin1234")
        every { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowNetworkError)) } answers { callOriginal() }
        coEvery { authenticator.signUp(params.email, params.password) } returns Either.Left(Failure.NetworkConnection)
        signUpViewModel.events.observeForever(eventObserver)
        signUpViewModel.signUp(params.email, params.password)
        verify { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpStart)) }
        verify { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowNetworkError)) }
    }

    @Test
    fun `singUpViewModel when signUp with serverError should update livedata with serverError`() = runTest {
        val params = SignUp.Params("admin@mail.com", "admin1234")
        every { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpStart)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowServerError)) } answers { callOriginal() }
        coEvery { authenticator.signUp(params.email, params.password) } returns Either.Left(Failure.ServerError)
        signUpViewModel.events.observeForever(eventObserver)
        signUpViewModel.signUp(params.email, params.password)
        verify { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowSignUpStart)) }
        verify { eventObserver.onChanged(Event(SignUpViewModel.Navigation.ShowServerError)) }
    }


}