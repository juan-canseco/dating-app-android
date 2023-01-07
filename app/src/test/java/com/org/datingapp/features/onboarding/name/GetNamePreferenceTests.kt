package com.org.datingapp.features.onboarding.name

import com.org.datingapp.UnitTest
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class GetNamePreferenceTests : UnitTest() {
    @RelaxedMockK
    private lateinit var preferences: SessionPreferences
    private lateinit var getNamePreference: GetNamePreference

    @Before
    fun setup() {
        getNamePreference = GetNamePreference(preferences)
    }

    @Test
    fun `getNamePreference when execute either should be right`() {
        val name = "John Doe"
        every { preferences.name } returns name
        runBlocking {
            val params = UseCase.None()
            val result = getNamePreference.run(params)
            result.isRight shouldBe true
        }
    }
}