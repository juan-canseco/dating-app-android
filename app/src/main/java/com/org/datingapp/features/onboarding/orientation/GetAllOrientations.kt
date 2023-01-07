package com.org.datingapp.features.onboarding.orientation

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.org.datingapp.core.domain.user.details.Orientation
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetAllOrientations
@Inject
constructor(
    @ApplicationContext
    private val context : Context,
    private val gson : Gson) : UseCase<List<Orientation>, UseCase.None>() {

    private fun getOrientations() : MutableList<Orientation> {
        val jsonString = context.assets.open("orientations.json")
            .bufferedReader()
            .use { it.readText() }
        val itemType = object : TypeToken<MutableList<Orientation>>() {}.type
        return gson.fromJson(jsonString, itemType)
    }

    override suspend fun run(params: None): Either<Failure, List<Orientation>> {
        return Either.Right(getOrientations())
    }
}