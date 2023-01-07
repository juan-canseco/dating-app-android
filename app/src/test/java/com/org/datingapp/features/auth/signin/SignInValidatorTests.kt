package com.org.datingapp.features.auth.signin

import com.org.datingapp.UnitTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class SignInValidatorTests : UnitTest() {

    private lateinit var signInValidator: SignInValidator

    @Before
    fun setup() {
        signInValidator = SignInValidator()
    }

    @Test
    fun `signInValidator when email is valid returns true`() {
        val validEmail = "admin1234@gmail.com"
        val result = signInValidator.isValidEmail(validEmail)
        result shouldBe true
    }

    @Test
    fun `signInValidator when email is invalid returns false`() {
        val invalidEmail = "admin@mailcom"
        val result = signInValidator.isValidEmail(invalidEmail)
        result shouldBe false
    }

    @Test
    fun `singInvalidator when password is valid returns true`() {
        val validPassword = "admin1234"
        val result = signInValidator.isValidPassword(validPassword)
        result shouldBe true
    }

    @Test
    fun `signInValidator when password is invalid returns false`() {
        val invalidPassword = "adm"
        val result = signInValidator.isValidPassword(invalidPassword)
        result shouldBe false
    }

}