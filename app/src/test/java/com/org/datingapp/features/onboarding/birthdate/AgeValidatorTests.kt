package com.org.datingapp.features.onboarding.birthdate

import com.org.datingapp.UnitTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class AgeValidatorTests : UnitTest() {

    private lateinit var ageValidator: AgeValidator

    @Before
    fun setup() {
        ageValidator = AgeValidator()
    }

    @Test
    fun `ageValidator when age is valid returns true`() {
        val age = 18
        val result = ageValidator.isValid(age)
        result shouldBe true
    }

    @Test
    fun `ageValidator when age is invalid returns false`() {
        val age = 17
        val result = ageValidator.isValid(age)
        result shouldBe false
    }
}