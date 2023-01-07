package com.org.datingapp.features.onboarding.interests

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.org.datingapp.core.domain.user.details.Interest
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetAllInterests
@Inject
constructor(
    @ApplicationContext
    private val context : Context,
    private val gson : Gson) : UseCase<List<Interest>, UseCase.None>() {

    private fun getInterests() : MutableList<Interest> {
        val jsonString = context.assets.open("interests.json")
            .bufferedReader()
            .use { it.readText() }
        val itemType = object : TypeToken<MutableList<Interest>>() {}.type
        return gson.fromJson(jsonString, itemType)
    }

    override suspend fun run(params: None): Either<Failure, List<Interest>> {
        return Either.Right(getInterests())
    }
}