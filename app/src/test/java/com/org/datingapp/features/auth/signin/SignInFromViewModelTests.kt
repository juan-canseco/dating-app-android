package com.org.datingapp.features.auth.signin

import androidx.lifecycle.Observer
import com.org.datingapp.AndroidTest
import com.org.datingapp.core.platform.Event
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SignInFromViewModelTests : AndroidTest() {

    private lateinit var signInValidator : SignInValidator
    private lateinit var signInFormViewModel: SignInFormViewModel

    @MockK
    private lateinit var eventObserver: Observer<Event<SignInFormViewModel.Navigation>?>

    @Before
    fun setup() {
        signInValidator = SignInValidator()
        signInFormViewModel = SignInFormViewModel(signInValidator)
    }

    @Test
    fun `signInFormViewModel when credentials are valid should update livedata with validCredentials`() {
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidEmail)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidPassword)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidCredentials)) } answers { callOriginal() }
        signInFormViewModel.events.observeForever(eventObserver)
        signInFormViewModel.email = "admin@mail.com"
        signInFormViewModel.password = "admin1234"
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidEmail)) }
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidPassword)) }
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidCredentials)) }
    }

    @Test
    fun `signInFormViewModel when password is invalid should update livedata with invalidCredentials`() {
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidEmail)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidPassword)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidCredentials)) } answers { callOriginal() }
        signInFormViewModel.events.observeForever(eventObserver)
        signInFormViewModel.email = "admin@mail.com"
        signInFormViewModel.password = "ai"
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidEmail)) }
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidPassword)) }
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidCredentials)) }
    }

    @Test
    fun `signInFormViewModel when email is invalid should update livedata with invalidCredentials`() {
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidEmail)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidPassword)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidCredentials)) } answers { callOriginal() }
        signInFormViewModel.events.observeForever(eventObserver)
        signInFormViewModel.email = "adminmail.com"
        signInFormViewModel.password = "admin1234"
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidEmail)) }
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowValidPassword)) }
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidCredentials)) }
    }

    @Test
    fun `signInFormViewModel when email and password are invalid should update livedata with invalidCredentials`() {
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidEmail)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidPassword)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidCredentials)) } answers { callOriginal() }
        signInFormViewModel.events.observeForever(eventObserver)
        signInFormViewModel.email = "adminmas/c"
        signInFormViewModel.password = "ad"
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidEmail)) }
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidPassword)) }
        verify { eventObserver.onChanged(Event(SignInFormViewModel.Navigation.ShowInvalidCredentials)) }
    }

}