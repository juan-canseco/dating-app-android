package com.org.datingapp.features.onboarding.birthdate

import com.org.datingapp.core.domain.user.details.BirthDate
import org.joda.time.LocalDate
import org.joda.time.Years
import javax.inject.Inject

// https://javarevisited.blogspot.com/2016/10/how-to-get-number-of-months-and-years-between-two-dates-in-java.html#axzz7Ci3lOzXw

class AgeCalculator @Inject constructor(private val dateProvider: DateProvider) {

    fun getAge(year : Int, month : Int, day : Int) : Int {
        val birthDay = LocalDate(year, month, day)
        val today = dateProvider.getCurrentDate()
        return Years.yearsBetween(birthDay, today).years
    }

    fun getAge(birthDate : BirthDate) : Int {
        return getAge(birthDate.year, birthDate.month, birthDate.day)
    }
}