package com.org.datingapp.features.onboarding.description

import com.org.datingapp.UnitTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.lang.StringBuilder

@RunWith(value = Parameterized::class)
class DescriptionValidatorTests(var expectedResult : Boolean, var currentValue : String) : UnitTest() {

    private lateinit var descriptionValidator : DescriptionValidator

    @Before
    fun setup() {
        descriptionValidator = DescriptionValidator()
    }

    companion object {
        private fun getMaxLengthString() : String {
            val sb = StringBuilder()
            for (i in 1..300) {
                sb.append("a")
            }
            return sb.toString()
        }

        private fun getMaxLengthStringPlus() = getMaxLengthString() + "a"

        @Parameterized.Parameters @JvmStatic
        fun getDescriptions() = arrayOf(
            arrayOf(true, "Hi i am john doe, nice no meet you"),
            arrayOf(true, "a"),
            arrayOf(true, getMaxLengthString()),
            arrayOf(false, getMaxLengthStringPlus())
        )
    }

    @Test
    fun descriptionValidatorTest() {
        val result = descriptionValidator.isValid(currentValue)
        result shouldBe expectedResult
    }

}