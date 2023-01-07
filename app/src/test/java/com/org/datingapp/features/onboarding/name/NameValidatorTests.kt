package com.org.datingapp.features.onboarding.name

import com.org.datingapp.UnitTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class NameValidatorTests(var currentValue : Boolean, var currentName : String) : UnitTest() {

    private lateinit var nameValidator : NameValidator

    @Before
    fun setup() {
        nameValidator = NameValidator()
    }

    companion object {
        @Parameterized.Parameters @JvmStatic
        fun getNames() = arrayOf(
            arrayOf(true, "John Doe"),
            arrayOf(true, "Al"),
            arrayOf(true, "Al'Patin"),
            arrayOf(false, "John, Doe"),
            arrayOf(false, "John Doe 2"),
            arrayOf(false, "A")
        )
    }

    @Test
    fun nameValidatorTest() {
        val result = nameValidator.isValid(currentName)
        result shouldBe currentValue
    }

}