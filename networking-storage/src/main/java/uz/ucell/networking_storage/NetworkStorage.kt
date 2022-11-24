package uz.ucell.networking_storage

interface NetworkStorage {
    fun setRefreshToken(token: String)
    fun getRefreshToken(): Token

    fun setAccessToken(token: String)
    fun getAccessToken(): Token

    fun setDeviceId(deviceId: String)
    fun getDeviceId(): String?

    fun setUserAgent(userAgent: String)
    fun getUserAgent(): String?

    fun setMsisdn(msisdn: String)
    fun getMsisdn(): String?

    fun setLanguage(language: String)
    fun getLanguage(): String?

    fun setHost(host: String)
    fun getHost(): String?

    fun setRequestsLoggingEnabled(checked: Boolean)
    fun getRequestsLoggingEnabled(): Boolean

    fun dropTokens()

    fun setDevToken(token: String)
    fun getDevToken(): String?
}
