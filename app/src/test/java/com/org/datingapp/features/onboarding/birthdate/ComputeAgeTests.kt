package com.org.datingapp.features.onboarding.birthdate

import com.org.datingapp.UnitTest
import com.org.datingapp.core.domain.user.details.BirthDate
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ComputeAgeTests : UnitTest() {

    @MockK
    private lateinit var dateProvider: DateProvider
    private lateinit var ageValidator: AgeValidator
    private lateinit var ageCalculator:  AgeCalculator
    private lateinit var computeAge : ComputeAge

    @Before
    fun setup() {
        ageValidator = AgeValidator()
        ageCalculator = AgeCalculator(dateProvider)
        computeAge = ComputeAge(ageValidator, ageCalculator)
    }

    @Test
    fun `computeAge when validAge should return validResult`() {
        val currentDate = LocalDate(2022, 8, 18 )
        every { dateProvider.getCurrentDate() } returns currentDate
        val params = ComputeAge.Params(BirthDate(2002,8, 18))
        runTest {
            val result = computeAge.run(params)
            result.isRight shouldBe true
        }
    }

    @Test
    fun `computeAge when invalidAge should return failure`() {
        val currentDate = LocalDate(2022, 8, 18 )
        every { dateProvider.getCurrentDate() } returns currentDate
        val params = ComputeAge.Params(BirthDate(2005,8, 18))
        runTest {
            val result = computeAge.run(params)
            result.isLeft shouldBe true
        }
    }
}