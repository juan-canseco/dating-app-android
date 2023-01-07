package com.org.datingapp.features.onboarding.birthdate

import androidx.lifecycle.Observer
import com.org.datingapp.AndroidTest
import com.org.datingapp.MainDispatcherRule
import com.org.datingapp.core.platform.Event
import com.org.datingapp.core.domain.user.details.BirthDate
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BirthDateViewModelTests : AndroidTest() {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var ageValidator: AgeValidator
    private lateinit var ageCalculator: AgeCalculator
    private lateinit var birthDateViewModel: BirthDateViewModel

    private lateinit var currentDate : LocalDate

    @MockK
    private lateinit var dateProvider: DateProvider

    @MockK
    private lateinit var eventObserver: Observer<Event<BirthDateViewModel.Navigation>?>

    private lateinit var computeAge : ComputeAge


    @Before
    fun setup() {
        ageValidator = AgeValidator()
        ageCalculator = AgeCalculator(dateProvider)
        computeAge = ComputeAge(ageValidator, ageCalculator)
        birthDateViewModel = BirthDateViewModel(computeAge)
        currentDate = LocalDate(2022, 7, 20)
    }

    @Test
    fun `birthDateViewModel when setDate with valid date should update livedata with validAge`() = runTest {

        val age = 18
        val birthDate = BirthDate(2004, 7, 20)

        every { dateProvider.getCurrentDate() } returns currentDate
        every { eventObserver.onChanged(Event(BirthDateViewModel.Navigation.ShowValidAge(birthDate, age))) } answers { callOriginal() }

        birthDateViewModel.events.observeForever(eventObserver)

        birthDateViewModel.setDate(birthDate)

        verify { eventObserver.onChanged(Event(BirthDateViewModel.Navigation.ShowValidAge(birthDate, age))) }

    }

    @Test
    fun `birthDateViewModel when setDate with invalid date should update livedata with invalidAge`() = runTest {
        val birthDate = BirthDate(2005, 7, 20)

        every { dateProvider.getCurrentDate() } returns currentDate
        every { eventObserver.onChanged(Event(BirthDateViewModel.Navigation.ShowInvalidAge)) } answers { callOriginal() }

        birthDateViewModel.events.observeForever(eventObserver)

        birthDateViewModel.setDate(birthDate)


        verify { eventObserver.onChanged(Event(BirthDateViewModel.Navigation.ShowInvalidAge)) }

    }

}