package com.org.datingapp.features.onboarding.interests

import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetInterestsPreference
@Inject
constructor(private val preferencesManager: SessionPreferences) :
    UseCase<HashSet<Interest>, UseCase.None> () {

    override suspend fun run(params: None): Either<Failure, HashSet<Interest>> {
        val interest = preferencesManager.interests
        return Either.Right(interest)
    }
}