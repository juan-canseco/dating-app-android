package com.org.datingapp.features.auth.signup

import com.org.datingapp.AndroidTest
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class SignUpValidatorTests : AndroidTest() {

    private lateinit var signUpValidator : SignUpValidator

    @Before
    fun setup() {
        signUpValidator = SignUpValidator()
    }

    @Test
    fun `signUpValidator when email is valid returns true`() {
        val validEmail = "admin1234@gmail.com"
        val result = signUpValidator.isValidEmail(validEmail)
        result shouldBe true
    }

    @Test
    fun `signUpValidator when email is invalid returns false`() {
        val invalidEmail = "admin@mailcom"
        val result = signUpValidator.isValidEmail(invalidEmail)
        result shouldBe false
    }

    @Test
    fun `singUpValidator when password is valid returns true`() {
        val validPassword = "admin1234"
        val result = signUpValidator.isValidPassword(validPassword)
        result shouldBe true
    }

    @Test
    fun `signUpValidator when password is invalid returns false`() {
        val invalidPassword = "adm"
        val result = signUpValidator.isValidPassword(invalidPassword)
        result shouldBe false
    }


    @Test
    fun `signUpValidator when confirmation password is valid returns true`() {
        val password = "admin1234"
        val confirmationPassword = "admin1234"
        val result = signUpValidator.isValidPasswordConfirmation(password, confirmationPassword)
        result shouldBe true
    }

    @Test
    fun `signUpValidator when confirmation password is invalid returns false`() {
        val password = "admin1234"
        val confirmationPassword = "admin123433"
        val result = signUpValidator.isValidPasswordConfirmation(password, confirmationPassword)
        result shouldBe false
    }
}