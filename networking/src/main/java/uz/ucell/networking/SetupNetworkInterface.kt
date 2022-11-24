package uz.ucell.networking

interface SetupNetworkInterface {
    fun setLocalization(localization: String)
    fun setBaseUrl(url: String)
    fun getBaseUrl(): String?
    fun setDeviceId(deviceId: String)
    fun setUserAgent(userAgent: String)
    fun setRequestsLoggingEnabled(checked: Boolean)
    fun getRequestsLoggingEnabled(): Boolean
    fun dropTokens()
    fun setDevToken(token: String)
    fun getDevToken(): String?
}
