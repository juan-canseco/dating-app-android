package com.org.datingapp.features.onboarding.name

import androidx.lifecycle.Observer
import com.org.datingapp.AndroidTest
import com.org.datingapp.core.platform.Event
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class NameViewModelTests : AndroidTest() {

    private lateinit var nameValidator: NameValidator
    private lateinit var nameViewModel: NameViewModel

    @MockK
    private lateinit var eventObserver : Observer<Event<NameViewModel.Navigation>?>

    @Before
    fun setup() {
        nameValidator = NameValidator()
        nameViewModel = NameViewModel(nameValidator)
    }

    @Test
    fun `nameViewModel when validName should update livedata with validName`() {
        every { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameValid)) } answers { callOriginal() }
        nameViewModel.events.observeForever(eventObserver)
        val name = "John Doe"
        nameViewModel.fullName = name
        verify { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameValid)) }
    }

    @Test
    fun `nameViewModel when validate with valid name should return true`() {
        val name = "John Doe"
        nameViewModel.fullName = name
        val result = nameViewModel.validate()
        result shouldBe true
    }

    @Test
    fun `nameViewModel when invalidName should update livedata with invalidName`() {
        every { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameInvalid)) } answers { callOriginal() }
        nameViewModel.events.observeForever(eventObserver)
        val name = "John Doe2"
        nameViewModel.fullName = name
        verify { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameInvalid)) }
    }

    @Test
    fun `nameViewModel when validate with invalid name should return false`() {
        val name = "John Doe2"
        nameViewModel.fullName = name
        val result = nameViewModel.validate()
        result shouldBe false
    }

    @Test
    fun `nameViewModel when validate with empty name should update livedata with nameRequiredError`() {
        every { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameRequired)) } answers { callOriginal() }
        nameViewModel.events.observeForever(eventObserver)
        nameViewModel.validate()
        verify { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameRequired)) }
    }

    @Test
    fun `nameViewModel when validate with invalid name should update livedata with nameError`() {
        every { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameError)) } answers { callOriginal() }
        nameViewModel.events.observeForever(eventObserver)
        nameViewModel.fullName = "John Doe 2"
        nameViewModel.validate()
        verify { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameError)) }
    }

    @Test
    fun `nameViewModel when validate with empty name and then name changes should update livedata with clearNameErrors`() {
        every { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameRequired)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameClearErrors)) } answers { callOriginal() }
        nameViewModel.events.observeForever(eventObserver)
        nameViewModel.validate()
        nameViewModel.fullName = "John"
        verify { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameRequired)) }
        verify { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameClearErrors)) }
    }

    @Test
    fun `nameViewModel when validate with invalid name and then name changes should update livedata with clearNameErrors`() {
        every { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameInvalid)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameClearErrors)) } answers { callOriginal() }
        nameViewModel.events.observeForever(eventObserver)
        nameViewModel.fullName = "John2"
        nameViewModel.validate()
        nameViewModel.fullName = "John"
        verify { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameInvalid)) }
        verify { eventObserver.onChanged(Event(NameViewModel.Navigation.ShowFullNameClearErrors)) }
    }

}