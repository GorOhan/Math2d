package com.ohanyan.mathgame.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class ConnectivityManagerNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkMonitor {
    override val isOnline: Flow<Boolean> = callbackFlow @androidx.annotation.RequiresPermission(
        android.Manifest.permission.ACCESS_NETWORK_STATE
    ) {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            channel.trySend(false)
            close()
            return@callbackFlow
        }

        /**
         * The callback's methods are invoked on changes to *any* network matching the [NetworkRequest],
         * not just the active network. So we can simply track the presence (or absence) of such [Network].
         */
        val callback = object : ConnectivityManager.NetworkCallback() {

            private val validNetworks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                // Determine if this network has internet
                launch(Dispatchers.IO) {
                    val hasInternet = checkInternetConnectivity()
                    if (hasInternet) {
                        synchronized(validNetworks) {
                            validNetworks += network
                        }
                        channel.trySend(true)
                    }
                }
            }

            override fun onLost(network: Network) {
                synchronized(validNetworks) {
                    validNetworks -= network
                    channel.trySend(validNetworks.isNotEmpty())
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val isInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                if (isInternet && isValidated) {
                    launch(Dispatchers.IO) {
                        val hasInternet = checkInternetConnectivity()
                        synchronized(validNetworks) {
                            if (hasInternet) {
                                validNetworks += network
                            } else {
                                validNetworks -= network
                            }
                            channel.trySend(validNetworks.isNotEmpty())
                        }
                    }
                }
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)

        /**
         * Sends the latest connectivity status to the underlying channel.
         */
        launch(Dispatchers.IO) {
            val isConnected = checkInternetConnectivity()
            channel.trySend(isConnected)
        }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
        .conflate()

    private suspend fun checkInternetConnectivity(): Boolean = withContext(Dispatchers.IO) {
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}
