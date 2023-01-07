package com.org.datingapp.features.auth.signout

import android.content.Context
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.features.onboarding.SessionPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SignOut
@Inject
constructor(private val sessionPreferences: SessionPreferences,
            @ApplicationContext
            private val context : Context) : UseCase<UseCase.None, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, None> {
        FirebaseAuth.getInstance().signOut()
        sessionPreferences.clearPreferences()
        Glide.get(context).clearDiskCache()
        return Either.Right(None())
    }
}