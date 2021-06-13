package com.umbertop.appcore.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

sealed class InternetUtil(context: Context) : LiveData<Boolean>() {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val defaultNetworkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .build()

    private val networkMonitor: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Timber.d("Connection available")

                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val hasInternetCapability =
                    networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

                Timber.d("onAvailable: $network, $hasInternetCapability")

                if (hasInternetCapability == true) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val hasInternet = DoesNetworkHaveInternet.execute()

                        if (hasInternet) {
                            withContext(Dispatchers.Main) {
                                Timber.d("$network has Internet connection")
                                validNetworks.add(network)
                                postValue(validNetworks.size > 0)
                            }
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Timber.d("Connection lost")

                validNetworks.remove(network)
                postValue(validNetworks.size > 0)
            }
        }

    private val validNetworks: MutableSet<Network> = HashSet()

    fun isInternetAvailable(): Boolean = validNetworks.size > 0

    override fun onActive() {
        super.onActive()
        connectivityManager.registerNetworkCallback(defaultNetworkRequest, networkMonitor)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkMonitor)
    }

    private object DoesNetworkHaveInternet {
        fun execute(): Boolean {
            return try {
                Timber.d("PINGING google")

                Socket().run {
                    connect(InetSocketAddress("8.8.8.8", 53), 1500)
                    close()
                }

                Timber.d("PING success")

                true
            } catch (e: IOException) {
                Timber.w(e, "No internet connection available")
                false
            }
        }
    }
}