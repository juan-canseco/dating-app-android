package com.org.datingapp.features.onboarding.birthdate

import org.joda.time.LocalDate
import javax.inject.Inject

interface DateProvider {
    fun getCurrentDate() : LocalDate

    class Implementation @Inject constructor() : DateProvider {
        override fun getCurrentDate(): LocalDate = LocalDate.now()
    }
}