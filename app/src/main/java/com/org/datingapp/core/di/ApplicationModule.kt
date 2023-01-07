package com.org.datingapp.core.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import androidx.work.WorkManager
import com.google.gson.Gson
import com.org.datingapp.core.utils.CoroutineDispatchersProvider
import com.org.datingapp.core.utils.DispatchersProvider
import com.org.datingapp.features.auth.Authenticator
import com.org.datingapp.features.home.profile.ProfileManager
import com.org.datingapp.features.location.LocationService
import com.org.datingapp.features.onboarding.SessionPreferences
import com.org.datingapp.features.onboarding.photos.PhotoUploader
import com.org.datingapp.features.onboarding.birthdate.DateProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier

@Qualifier
annotation class SessionSharedPreferences

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @SessionSharedPreferences
    @Provides
    @Singleton
    fun providesSessionPreferences(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("dating-app-session-preferences" , Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesWorkerManager(@ApplicationContext context : Context) = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun providesAuthenticator(authenticator : Authenticator.Firebase) : Authenticator = authenticator


    @Provides
    @Singleton
    fun providesProfileManager(profileManager: ProfileManager.Implementation) : ProfileManager = profileManager

    @Provides
    @Singleton
    fun providesLocationService(locationService: LocationService.Implementation) : LocationService = locationService

    @Singleton
    @Provides
    fun providesSessionPreferencesManager(preferences : SessionPreferences.Implementation) : SessionPreferences = preferences

    @Provides
    @Singleton
    fun providesDateProvider(dateProvider : DateProvider.Implementation) : DateProvider = dateProvider

    @Provides
    @Singleton
    fun providesPhotoUploader(photoUploader: PhotoUploader.Firebase) : PhotoUploader = photoUploader

    @Provides
    @Singleton
    fun providesGSON() = Gson()

    @Provides
    @Singleton
    fun providesCoroutinesDispatchers(dispatchersProvider: CoroutineDispatchersProvider) : DispatchersProvider = dispatchersProvider
}