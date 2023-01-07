package com.org.datingapp.features.onboarding.description

import com.org.datingapp.UnitTest
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class GetDescriptionPreferenceTests : UnitTest() {

    @RelaxedMockK
    private lateinit var preferences: SessionPreferences
    private lateinit var getDescriptionPreference: GetDescriptionPreference

    @Before
    fun setup() {
        getDescriptionPreference = GetDescriptionPreference(preferences)
    }

    @Test
    fun `getDescriptionPreference when execute either should be right`() {
        val description = "John Doe"
        every { preferences.description } returns description
        runBlocking {
            val params = UseCase.None()
            val result = getDescriptionPreference.run(params)
            result.isRight shouldBe true
        }
    }

}