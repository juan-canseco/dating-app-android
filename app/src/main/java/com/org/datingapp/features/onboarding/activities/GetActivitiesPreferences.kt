package com.org.datingapp.features.onboarding.activities

import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class GetActivitiesPreferences
@Inject
constructor(private val preferencesManager: SessionPreferences) :
    UseCase<HashSet<Activity>, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, HashSet<Activity>> {
        val activities = preferencesManager.activities
        return Either.Right(activities)
    }
}