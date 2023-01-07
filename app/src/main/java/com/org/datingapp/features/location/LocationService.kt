package com.org.datingapp.features.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.org.datingapp.features.onboarding.UserLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LocationService {

    @Throws(Exceptions.PermissionNotGranted::class)
    suspend fun getCurrentLocation(): UserLocation

    class Exceptions {
        class PermissionNotGranted : Exception()
    }

    class Implementation @Inject constructor(
        @ApplicationContext
        private val context: Context
    ) : LocationService {

        private val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        private val cancellationToken: CancellationToken = object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                CancellationTokenSource().token

            override fun isCancellationRequested() = false
        }

        override suspend fun getCurrentLocation(): UserLocation {
            return suspendCoroutine { continuation ->

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    continuation.resumeWithException(Exceptions.PermissionNotGranted())
                }
                fusedLocationClient
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken)
                    .addOnSuccessListener { location ->
                        if (location == null) {
                            continuation.resume(UserLocation())
                        } else {
                            continuation.resume(
                                UserLocation(location.latitude, location.longitude)
                            )
                        }
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }
    }

}


