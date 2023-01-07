package com.org.datingapp.features.onboarding.activities

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.org.datingapp.core.domain.user.details.Activity
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetAllActivities
@Inject
constructor(
    @ApplicationContext
    private val context : Context,
    private val gson : Gson): UseCase<List<Activity>, UseCase.None>() {

    private fun getActivities() : MutableList<Activity> {
        val jsonString = context.assets.open("activities.json")
            .bufferedReader()
            .use { it.readText() }
        val itemType = object : TypeToken<MutableList<Activity>>() {}.type
        return gson.fromJson(jsonString, itemType)
    }

    override suspend fun run(params: None): Either<Failure, List<Activity>> {
        return Either.Right(getActivities())
    }
}