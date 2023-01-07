package com.org.datingapp.features.onboarding.description

import androidx.lifecycle.Observer
import com.org.datingapp.AndroidTest
import com.org.datingapp.core.platform.Event
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class DescriptionViewModelTests : AndroidTest() {

    private lateinit var descriptionValidator: DescriptionValidator
    private lateinit var descriptionViewModel: DescriptionViewModel

    @MockK
    private lateinit var eventObserver : Observer<Event<DescriptionViewModel.Navigation>?>

    @Before
    fun setup() {
        descriptionValidator = DescriptionValidator()
        descriptionViewModel = DescriptionViewModel(descriptionValidator)
    }

    private fun largeDescription() : String {
        val sb = StringBuilder()
        for (i in 0..301) {
            sb.append("a")
        }
        return sb.toString()
    }

    @Test
    fun `descriptionViewModel when valid description should update livedata with validDescription`() {
        every { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowValidDescription)) } answers { callOriginal() }
        descriptionViewModel.events.observeForever(eventObserver)
        descriptionViewModel.description = "Hello, I am John Doe, Nice to meet you"
        verify { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowValidDescription)) }
    }

    @Test
    fun `descriptionViewModel when validate with valid description should return true`() {
        descriptionViewModel.description = "Hello, I am John Doe, Nice to meet you"
        val result = descriptionViewModel.validate()
        result shouldBe true
    }

    @Test
    fun `descriptionViewModel when invalidDescription should update livedata with invalidDescription`() {
        every { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowInvalidDescription)) } answers { callOriginal() }
        descriptionViewModel.events.observeForever(eventObserver)
        descriptionViewModel.description = largeDescription()
        verify { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowInvalidDescription)) }
    }

    @Test
    fun `descriptionViewModel when validate with invalid description should return false`() {
        descriptionViewModel.description = largeDescription()
        val result = descriptionViewModel.validate()
        result shouldBe false
    }

    @Test
    fun `descriptionViewModel when validate with empty description should update livedata with descriptionRequiredError`() {
        every { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowRequiredDescriptionError)) } answers { callOriginal() }
        descriptionViewModel.events.observeForever(eventObserver)
        descriptionViewModel.validate()
        verify { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowRequiredDescriptionError)) }
    }

    @Test
    fun `descriptionViewModel when validate with invalid description should update livedata with descriptionError`() {
        every { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowDescriptionError)) } answers { callOriginal() }
        descriptionViewModel.events.observeForever(eventObserver)
        descriptionViewModel.description = largeDescription()
        descriptionViewModel.validate()
        verify { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowDescriptionError)) }
    }

    @Test
    fun `descriptionViewModel when validate with empty description and then description changes should update livedata with clearDescriptionErrors`() {
        every { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowRequiredDescriptionError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowDescriptionClearErrors)) } answers { callOriginal() }
        descriptionViewModel.events.observeForever(eventObserver)
        descriptionViewModel.validate()
        descriptionViewModel.description = "Hello, I am John Doe, Nice to meet you"
        verify { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowRequiredDescriptionError)) }
        verify { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowDescriptionClearErrors)) }
    }

    @Test
    fun `descriptionViewModel when validate with invalid description and then description changes should update livedata with clearDescriptionErrors`() {
        every { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowDescriptionError)) } answers { callOriginal() }
        every { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowDescriptionClearErrors)) } answers { callOriginal() }
        descriptionViewModel.description = largeDescription()
        descriptionViewModel.events.observeForever(eventObserver)
        descriptionViewModel.validate()
        descriptionViewModel.description = "Hello, I am John Doe, Nice to meet you"
        verify { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowDescriptionError)) }
        verify { eventObserver.onChanged(Event(DescriptionViewModel.Navigation.ShowDescriptionClearErrors)) }
    }


}