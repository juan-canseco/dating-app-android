package com.org.datingapp.features.onboarding

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.org.datingapp.AndroidTest
import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.features.onboarding.photos.Photo
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.junit.After
import org.junit.Before
import org.junit.Test

class SessionSharedPrefsTests : AndroidTest() {

    private lateinit var context : Context
    private lateinit var sharedPref : SharedPreferences
    private lateinit var preferencesManager: SessionPreferences.Implementation
    private lateinit var gson : Gson

    @Before
    fun setup() {
        context = context()
        gson = Gson()
        sharedPref = context.getSharedPreferences("on-boarding-test-preferences", Context.MODE_PRIVATE)
        preferencesManager = SessionPreferences.Implementation(sharedPref, gson)
    }

    @Test
    fun `onBoardingPreferencesManager set fullName should returns fullName`() {
        val name = "John Doe"
        preferencesManager.name = name
        preferencesManager.name shouldBe name
    }

    @Test
    fun `onBoardingPreferencesManager set birthDate should return birthDate`() {
        val birthDate = BirthDate(1994, 5, 4 )
        preferencesManager.birthDate = birthDate
        preferencesManager.birthDate shouldEqual birthDate
    }


    @Test
    fun `onBoardingPreferencesManager set gender should returns gender`() {
        val gender = Gender(1, "Man")
        preferencesManager.gender = gender
        preferencesManager.gender shouldEqual gender
    }

    @Test
    fun `onBoardingPreferencesManager set username should returns username`() {
        val username = "jcanseco"
        preferencesManager.username = username
        preferencesManager.username shouldBe username
    }

    @Test
    fun `onBoardingPreferencesManager set description should returns description`() {
        val description = "Hello, I am john doe, nice to meet you"
        preferencesManager.description = description
        preferencesManager.description shouldBe description
    }

    @Test
    fun `onBoardingPreferencesManager set interests should return interests`() {
        val interests = hashSetOf(
            Interest("some-id-1", "Chill"),
            Interest("some-id-2","Coffee"),
            Interest("some-id-3","Video Games"))
        preferencesManager.interests = interests
        preferencesManager.interests shouldEqual interests
    }

    @Test
    fun `onBoardingPreferencesManager set photos should return photos`() {

        val photos = mutableListOf(
            Photo("https://www.fakedomain.com/profilepictures/picture1-local.jpg",
                "https://www.fakedomain.com/profilepictures/picture1-remote.jpg"),
            Photo("https://www.fakedomain.com/profilepictures/picture2-local.jpg",
                "https://www.fakedomain.com/profilepictures/picture2-remote.jpg"))

        preferencesManager.photos = photos
        preferencesManager.photos shouldEqual photos
    }

    @After
    fun teardown() {
        preferencesManager.clearPreferences()
    }
}