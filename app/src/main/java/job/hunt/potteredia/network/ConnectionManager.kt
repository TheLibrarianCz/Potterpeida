package job.hunt.potteredia.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import job.hunt.potteredia.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val applicationScope: CoroutineScope
) {
    private val capableNetworks: MutableSet<Network> = HashSet()

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private val googleDnsAddress: InetSocketAddress = InetSocketAddress("8.8.8.8", 53)

    private val networkRequest =
        NetworkRequest.Builder().addCapability(NET_CAPABILITY_INTERNET).build()

    private val networkCallback: ConnectivityManager.NetworkCallback = createNetworkCallback()

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val isConnected: Flow<Boolean> = _isConnected.asStateFlow()

    val isCurrentlyConnected: Boolean
        get() = _isConnected.value

    init {
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Timber.i("$TAG#onAvailable()")

            applicationScope.launch {
                verifyNetwork(network)
            }
        }

        override fun onLost(network: Network) {
            Timber.i("$TAG#onLost()")
            super.onLost(network)
            capableNetworks.remove(network)
            _isConnected.value = capableNetworks.isNotEmpty()
        }
    }

    private suspend fun verifyNetwork(network: Network): Unit = withContext(Dispatchers.IO) {
        if (checkConnectivity(network)) {
            capableNetworks.add(network)
            _isConnected.value = capableNetworks.isNotEmpty()
        } else {
            Timber.w("$TAG Could not verify connection for the given network.")
        }
    }

    private suspend fun checkConnectivity(network: Network): Boolean = withContext(Dispatchers.IO) {
        var socketCreated = false
        var successfulPing = false
        try {
            val socket: Socket = network.socketFactory.createSocket()
            socketCreated = true
            socket.connect(googleDnsAddress, CONNECTIVITY_CHECK_TIMEOUT_IN_MS)
            successfulPing = true
            socket.close()
            Timber.v("$TAG Successful check for the connectivity.")
            true
        } catch (e: IOException) {
            /*when {
                successfulPing -> Timber.e("$TAG IO Exception while trying to close the socket.")
                socketCreated -> Timber.e("$TAG Could not reach internet.")
                else -> Timber.e("$TAG Unspecified IO Exception($e).")
            }*/
            false
        }
    }

    companion object {
        private const val TAG = "ConnectionObserver"

        private const val CONNECTIVITY_CHECK_TIMEOUT_IN_MS = 2000
    }
}
