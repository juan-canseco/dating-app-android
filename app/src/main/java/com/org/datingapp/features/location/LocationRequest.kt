package com.org.datingapp.features.location

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.interactor.UseCase.None
import com.org.datingapp.features.onboarding.SessionPreferences
import kotlinx.coroutines.delay
import javax.inject.Inject

class LocationRequest @Inject constructor(private val locationService: LocationService,
                                          private val preferencesManager: SessionPreferences) : UseCase<None, None>() {

    override suspend fun run(params: None): Either<Failure, None> {
        return try {
            delay(2000L)
            val userLocation = locationService.getCurrentLocation()
            preferencesManager.location = userLocation
            return Either.Right(None())
        }
        catch (exception : Exception) {
            when (exception) {
                is LocationService.Exceptions.PermissionNotGranted -> {
                    Either.Left(Features.LocationPermissionNotGranted())
                }
                else -> {
                    Either.Left(Failure.ServerError)
                }
            }
        }
    }

    class Features {
        class LocationPermissionNotGranted : Failure.FeatureFailure()
    }
}