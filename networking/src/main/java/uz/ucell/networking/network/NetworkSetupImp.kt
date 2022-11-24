package uz.ucell.networking.network

import uz.ucell.networking.BuildConfig.BASE_URL
import uz.ucell.networking.BuildConfig.DEVELOPMENT_TOKEN
import uz.ucell.networking.SetupNetworkInterface
import uz.ucell.networking_storage.NetworkStorage

internal class NetworkSetupImp(private val storage: NetworkStorage) : SetupNetworkInterface {

    init {
        initCacheHost()
        initDevToken()
    }

    override fun setLocalization(localization: String) {
        storage.setLanguage(localization)
    }

    override fun setBaseUrl(url: String) {
        storage.setHost(url)
    }

    override fun getBaseUrl() = storage.getHost()

    override fun setDeviceId(deviceId: String) {
        storage.setDeviceId(deviceId)
    }

    override fun setUserAgent(userAgent: String) {
        storage.setUserAgent(userAgent)
    }

    override fun setRequestsLoggingEnabled(checked: Boolean) {
        storage.setRequestsLoggingEnabled(checked)
    }

    override fun getRequestsLoggingEnabled() = storage.getRequestsLoggingEnabled()

    override fun dropTokens() {
        storage.dropTokens()
    }

    override fun setDevToken(token: String) {
        storage.setDevToken(token)
    }

    override fun getDevToken(): String? =
        storage.getDevToken()

    private fun initCacheHost() {
        val cachedHost = storage.getHost()
        if (cachedHost.isNullOrEmpty()) {
            storage.setHost(BASE_URL)
        }
    }

    private fun initDevToken() {
        storage.setDevToken(DEVELOPMENT_TOKEN)
    }
}
