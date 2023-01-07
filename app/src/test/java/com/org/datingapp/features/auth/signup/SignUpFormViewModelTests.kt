package com.org.datingapp.features.auth.signup

import androidx.lifecycle.Observer
import com.org.datingapp.AndroidTest
import com.org.datingapp.core.platform.Event
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class SignUpFormViewModelTests : AndroidTest() {

    private lateinit var signUpFormViewModel: SignUpFormViewModel
    private lateinit var signUpValidator : SignUpValidator

    @MockK
    private lateinit var eventObserver: Observer<Event<SignUpFormViewModel.Navigation>?>

    @Before
    fun setup() {
        signUpValidator = SignUpValidator()
        signUpFormViewModel = SignUpFormViewModel(signUpValidator)
    }

    @Test
    fun `signUpFormViewModel when validate with valid form fields returns true`() {
        signUpFormViewModel.email = "admin@mail.com"
        signUpFormViewModel.password = "admin1234"
        signUpFormViewModel.passwordConfirmation = "admin1234"
        val result = signUpFormViewModel.validate()
        result shouldBe true
    }


    @Test
    fun `signUpFormViewModel when validate with invalid form fields returns false`() {
        signUpFormViewModel.email = "admin@mail.com"
        signUpFormViewModel.password = "admin1234"
        signUpFormViewModel.passwordConfirmation = "admi234"
        val result = signUpFormViewModel.validate()
        result shouldBe false
    }

    @Test
    fun `signUpFormViewModel when validate with empty form fields should update livedata with invalid requiredFields`() {
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowEmailRequiredError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordRequiredError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordConfirmationRequiredError)) } answers { callOriginal() }

        signUpFormViewModel.events.observeForever(eventObserver)

        signUpFormViewModel.validate()

        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowEmailRequiredError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordRequiredError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordConfirmationRequiredError)) }
    }

    @Test
    fun `signUpFormViewModel when validate with error form fields should update livedata with invalid errorFields`() {
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowEmailError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordConfirmationError)) } answers { callOriginal() }

        signUpFormViewModel.events.observeForever(eventObserver)

        signUpFormViewModel.email = "sds"
        signUpFormViewModel.password = "1"
        signUpFormViewModel.passwordConfirmation = "0"

        signUpFormViewModel.validate()

        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowEmailError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordConfirmationError)) }
    }

    @Test
    fun `signUpFormViewModel when validate with required form fields then clean form fields should update livedata with clearFieldsErrors`() {

        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowEmailRequiredError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordRequiredError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordConfirmationRequiredError)) } answers { callOriginal() }

        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearEmailErrors)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearPasswordErrors)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearPasswordConfirmationErrors)) } answers { callOriginal() }


        signUpFormViewModel.events.observeForever(eventObserver)

        signUpFormViewModel.validate()

        signUpFormViewModel.email = "email@mail.com"
        signUpFormViewModel.password = "admin1234"
        signUpFormViewModel.passwordConfirmation = "admin1234"

        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowEmailRequiredError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordRequiredError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordConfirmationRequiredError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearEmailErrors)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearPasswordErrors)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearPasswordConfirmationErrors)) }

    }

    @Test
    fun `signUpFormViewModel when validate with error form fields then clean form fields should update livedata with clearFieldsErrors`() {

        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowEmailError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordConfirmationError)) } answers { callOriginal() }

        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearEmailErrors)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearPasswordErrors)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearPasswordConfirmationErrors)) } answers { callOriginal() }


        signUpFormViewModel.events.observeForever(eventObserver)


        signUpFormViewModel.email = "email"
        signUpFormViewModel.password = "ad"
        signUpFormViewModel.passwordConfirmation = "a"

        signUpFormViewModel.validate()

        signUpFormViewModel.email = "email@mail.com"
        signUpFormViewModel.password = "admin1234"
        signUpFormViewModel.passwordConfirmation = "admin1234"

        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowEmailError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowPasswordConfirmationError)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearEmailErrors)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearPasswordErrors)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowClearPasswordConfirmationErrors)) }

    }


    @Test
    fun `signUpFormViewModel when all form fields are valid should update livedata with validFields`() {

        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowValidEmail)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowValidPassword)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowValidPasswordConfirmation)) } answers { callOriginal() }

        signUpFormViewModel.events.observeForever(eventObserver)

        signUpFormViewModel.email = "admin@mail.com"
        signUpFormViewModel.password = "admin1234"
        signUpFormViewModel.passwordConfirmation = "admin1234"

        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowValidEmail)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowValidPassword)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowValidPasswordConfirmation)) }
    }

    @Test
    fun `signUpFormViewModel when all fields are invalid should update livedata with invalidFields`(){
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowInvalidEmail)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowInvalidPassword)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowInvalidPasswordConfirmation)) } answers { callOriginal() }

        signUpFormViewModel.events.observeForever(eventObserver)

        signUpFormViewModel.email = "admin"
        signUpFormViewModel.password = "ad"
        signUpFormViewModel.passwordConfirmation = "d"

        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowInvalidEmail)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowInvalidPassword)) }
        verify { eventObserver.onChanged(Event(SignUpFormViewModel.Navigation.ShowInvalidPasswordConfirmation)) }
    }

}