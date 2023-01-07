package com.org.datingapp.features.onboarding.gender

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.org.datingapp.core.domain.user.details.Gender
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetAllGenders
@Inject
constructor(
    @ApplicationContext
    private val context : Context,
    private val gson : Gson) : UseCase<List<Gender>, UseCase.None>() {

    private fun getGenders() : MutableList<Gender> {
        val jsonString = context.assets.open("genders.json")
            .bufferedReader()
            .use { it.readText() }
        val itemType = object : TypeToken<MutableList<Gender>>() {}.type
        return gson.fromJson(jsonString, itemType)
    }


    override suspend fun run(params: None): Either<Failure, List<Gender>> {
        return Either.Right(getGenders())
    }

}