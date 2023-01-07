package com.org.datingapp.features.onboarding.name

import com.org.datingapp.UnitTest
import com.org.datingapp.features.onboarding.SessionPreferences
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class StoreNamePreferenceTests : UnitTest() {

    @RelaxedMockK
    private lateinit var preferences: SessionPreferences
    private lateinit var storeNamePreference: StoreNamePreference

    @Before
    fun setup() {
        storeNamePreference = StoreNamePreference(preferences)
    }

    @Test
    fun `storeNamePreference when execute either should be right`() {
        val name = "John Doe"
        runBlocking {
            val params = StoreNamePreference.Params(name)
            val result = storeNamePreference.run(params)
            result.isRight shouldBe true
        }
    }
}