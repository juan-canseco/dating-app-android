package com.org.datingapp.features.onboarding.username

import androidx.lifecycle.Observer
import com.org.datingapp.AndroidTest
import com.org.datingapp.core.platform.Event
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class UsernameViewModelTests : AndroidTest() {

    private lateinit var usernameValidator: UsernameValidator
    private lateinit var usernameViewModel: UsernameViewModel

    @MockK
    private lateinit var eventObserver : Observer<Event<UsernameViewModel.Navigation>?>

    @Before
    fun setup() {
        usernameValidator = UsernameValidator()
        usernameViewModel = UsernameViewModel(usernameValidator)
    }

    @Test
    fun `usernameViewModel when valid username should update livedata with validUsername`() {
        every { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowValidUsername)) } answers { callOriginal() }
        usernameViewModel.events.observeForever(eventObserver)
        usernameViewModel.username = "john_doe"
        verify { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowValidUsername)) }
    }

    @Test
    fun `usernameViewModel when validate with valid username should return true`() {
        usernameViewModel.username = "john_doe"
        val result = usernameViewModel.validate()
        result shouldBe true
    }

    @Test
    fun `descriptionViewModel when invalidDescription should update livedata with invalidUsername`() {
        every { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowInvalidUsername)) } answers { callOriginal() }
        usernameViewModel.events.observeForever(eventObserver)
        usernameViewModel.username = "94_john_doe"
        verify { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowInvalidUsername)) }
    }

    @Test
    fun `usernameViewModel when validate with invalid description should return false`() {
        usernameViewModel.username = "94_john_doe"
        val result = usernameViewModel.validate()
        result shouldBe false
    }

    @Test
    fun `usernameViewModel when validate with empty username should update livedata with usernameRequiredError`() {
        every { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowUsernameRequiredError)) } answers { callOriginal() }
        usernameViewModel.events.observeForever(eventObserver)
        usernameViewModel.validate()
        verify { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowUsernameRequiredError)) }
    }

    @Test
    fun `usernameViewModel when validate with invalid username should update livedata with usernameError`() {
        every { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowUsernameError)) } answers { callOriginal() }
        usernameViewModel.events.observeForever(eventObserver)
        usernameViewModel.username = "94_john_doe"
        usernameViewModel.validate()
        verify { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowUsernameError)) }
    }

    @Test
    fun `usernameViewModel when validate with empty username and then username changes should update livedata with clearUsernameErrors`() {
        every { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowUsernameRequiredError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowClearUsernameError)) } answers { callOriginal() }
        usernameViewModel.events.observeForever(eventObserver)
        usernameViewModel.validate()
        usernameViewModel.username = "john_dode"
        verify { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowUsernameRequiredError)) }
        verify { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowClearUsernameError)) }
    }

    @Test
    fun `usernameViewModel when validate with invalid username and then username changes should update livedata with clearUsernameErrors`() {
        every { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowUsernameError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowClearUsernameError)) } answers { callOriginal() }
        usernameViewModel.username = "92_john_doe"
        usernameViewModel.events.observeForever(eventObserver)
        usernameViewModel.validate()
        usernameViewModel.username = "john_doe"
        verify { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowUsernameError)) }
        verify { eventObserver.onChanged(Event(UsernameViewModel.Navigation.ShowClearUsernameError)) }
    }

}