package com.org.datingapp.features.onboarding.birthdate


import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.domain.user.details.BirthDate
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetBirthDatePreference @Inject constructor(private val preferencesManager: SessionPreferences) : UseCase<BirthDate, UseCase.None>() {

    override suspend fun run(params: None): Either.Right<BirthDate> {
        val birthDate = preferencesManager.birthDate ?: BirthDate.empty()
        return Either.Right(birthDate)
    }
}