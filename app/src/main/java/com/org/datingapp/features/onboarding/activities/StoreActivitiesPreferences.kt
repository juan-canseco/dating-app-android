package com.org.datingapp.features.onboarding.activities

import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import javax.inject.Inject

class StoreActivitiesPreferences
@Inject
constructor(private val preferencesManager: SessionPreferences) :
    UseCase<UseCase.None, StoreActivitiesPreferences.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        preferencesManager.activities = params.activities
        return Either.Right(None())
    }

    data class Params(val activities : HashSet<Activity>)
}