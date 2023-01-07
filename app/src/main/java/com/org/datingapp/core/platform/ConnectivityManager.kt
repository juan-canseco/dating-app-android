package com.org.datingapp.core.platform

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityManager
@Inject
constructor(@ApplicationContext
            context : Context) {
    private val connectionLiveData = ConnectionLiveData(context)

    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner, connectivityListener : (internetAvailable : Boolean) -> Unit){
        connectionLiveData.observe(lifecycleOwner) { isConnected ->
            isConnected?.let { connectivityListener(it) }
        }
    }

    fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.removeObservers(lifecycleOwner)
    }
}