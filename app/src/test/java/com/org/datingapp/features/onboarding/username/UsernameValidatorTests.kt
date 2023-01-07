package com.org.datingapp.features.onboarding.username

import com.org.datingapp.UnitTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(value = Parameterized::class)
class UsernameValidatorTests(var expectedResult : Boolean, var currentValue : String) : UnitTest() {

    private lateinit var usernameValidator: UsernameValidator

    @Before
    fun setup() {
        usernameValidator = UsernameValidator()
    }

    companion object {

        private fun getMaxLengthUsername() : String {
            val sb = StringBuilder()
            for (i in 1..30)
                sb.append("a")
            return sb.toString()
        }

        private fun getMaxLengthPlus() = getMaxLengthUsername() + "a"

        private fun getMinLengthUsername() : String {
            return "john1"
        }

        @Parameterized.Parameters @JvmStatic
        fun getNames() = arrayOf(
            arrayOf(true, "jhon_doe"),
            arrayOf(true, "john__doe__117"),
            arrayOf(true, "john_doe_117"),
            arrayOf(true, getMinLengthUsername()),
            arrayOf(true, getMaxLengthUsername()),
            arrayOf(false, getMaxLengthPlus()),
            arrayOf(false, "john'_doe_23"),
            arrayOf(false, "john__doe_117_")
        )
    }

    @Test
    fun usernameValidatorTest() {
        val result = usernameValidator.isValid(currentValue)
        result shouldBe expectedResult
    }
}