package com.org.datingapp.features.onboarding.birthdate

import com.org.datingapp.UnitTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.amshove.kluent.shouldBe
import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class AgeCalculatorTests(var expected : Int, var year : Int, var month : Int, var day : Int ) : UnitTest() {

    private lateinit var ageCalculator: AgeCalculator

    @MockK
    private lateinit var dateProvider: DateProvider

    private lateinit var currentDate : LocalDate

    @Before
    fun setup() {
        currentDate = LocalDate(2022, 7, 20)
        ageCalculator = AgeCalculator(dateProvider)
    }

    companion object {
        @Parameterized.Parameters @JvmStatic
        fun getParams() = arrayOf(
            arrayOf(18, 2004, 7, 20),
            arrayOf(28, 1994, 5, 4)
        )
    }

    @Test
    fun ageCalculatorTest() {
        every { dateProvider.getCurrentDate() } returns currentDate
        val age = ageCalculator.getAge(year, month, day)
        age shouldBe expected
    }
}