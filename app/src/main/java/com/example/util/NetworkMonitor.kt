package com.example.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class NetworkMonitor(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val _isOnline = MutableStateFlow(checkInitialConnectivity())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _networkType = MutableStateFlow(getInitialNetworkType())
    val networkType: StateFlow<String> = _networkType.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isOnline.value = true
            _networkType.value = getNetworkType(network)
        }

        override fun onLost(network: Network) {
            val connected = checkInitialConnectivity()
            _isOnline.value = connected
            _networkType.value = if (connected) getInitialNetworkType() else "Sin conexión"
        }

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            _isOnline.value = hasInternet
            _networkType.value = getNetworkType(network)
        }
    }

    init {
        try {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager?.registerNetworkCallback(request, networkCallback)
        } catch (_: Exception) {
            _isOnline.value = true
        }
    }

    private fun checkInitialConnectivity(): Boolean {
        return try {
            val activeNetwork = connectivityManager?.activeNetwork ?: return false
            val caps = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            true
        }
    }

    private fun getInitialNetworkType(): String {
        return try {
            val activeNetwork = connectivityManager?.activeNetwork ?: return "Desconectado"
            getNetworkType(activeNetwork)
        } catch (_: Exception) {
            "Internet"
        }
    }

    private fun getNetworkType(network: Network): String {
        val caps = connectivityManager?.getNetworkCapabilities(network) ?: return "Internet"
        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Datos móviles"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "En línea"
        }
    }

    suspend fun checkRealInternetPing(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://clients3.google.com/generate_204")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.requestMethod = "GET"
            val responseCode = connection.responseCode
            connection.disconnect()
            val available = (responseCode == 204 || responseCode == 200)
            _isOnline.value = available
            available
        } catch (_: Exception) {
            false
        }
    }
}
