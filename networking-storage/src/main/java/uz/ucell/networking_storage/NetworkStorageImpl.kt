package uz.ucell.networking_storage

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit

internal class NetworkStorageImpl(private val preferences: SharedPreferences) : NetworkStorage {
    @VisibleForTesting
    companion object Constants {
        @VisibleForTesting
        const val KEY_REFRESH_TOKEN = "uz.ucell.app.network.storage.refresh.token"

        @VisibleForTesting
        const val KEY_ACCESS_TOKEN = "uz.ucell.app.network.storage.access.token"

        @VisibleForTesting
        const val KEY_DEVICE_ID = "uz.ucell.app.network.storage.device.id"

        @VisibleForTesting
        const val KEY_USER_AGENT = "uz.ucell.app.network.storage.user.agent"

        @VisibleForTesting
        const val KEY_MSISDN = "uz.ucell.app.network.storage.msisdn"

        @VisibleForTesting
        const val KEY_LANGUAGE = "uz.ucell.app.network.storage.language"

        @VisibleForTesting
        const val KEY_HOST = "uz.ucell.app.network.storage.host"

        const val KEY_NETWORK_LOGGING_ENABLED = "uz.ucell.app.network.storage.network_logging"

        const val KEY_DEVELOPMENT_TOKEN = "uz.ucell.app.network.storage.dev.token"

        private const val DEFAULT = ""
    }

    override fun setRefreshToken(token: String) {
        token putIn KEY_REFRESH_TOKEN
    }

    override fun getRefreshToken(): Token =
        Token(KEY_REFRESH_TOKEN getOrElse DEFAULT)

    override fun setAccessToken(token: String) {
        token putIn KEY_ACCESS_TOKEN
    }

    override fun getAccessToken(): Token =
        Token(KEY_ACCESS_TOKEN getOrElse DEFAULT)

    override fun setDeviceId(deviceId: String) {
        deviceId putIn KEY_DEVICE_ID
    }

    override fun getDeviceId(): String? =
        KEY_DEVICE_ID getOrElse null

    override fun setUserAgent(userAgent: String) {
        userAgent putIn KEY_USER_AGENT
    }

    override fun getUserAgent(): String? =
        KEY_USER_AGENT getOrElse null

    override fun setMsisdn(msisdn: String) {
        msisdn putIn KEY_MSISDN
    }

    override fun getMsisdn(): String? =
        KEY_MSISDN getOrElse null

    override fun setLanguage(language: String) {
        language putIn KEY_LANGUAGE
    }

    override fun getLanguage(): String? =
        KEY_LANGUAGE getOrElse null

    override fun setHost(host: String) {
        host putIn KEY_HOST
    }

    override fun getHost(): String? =
        KEY_HOST getOrElse null

    override fun setRequestsLoggingEnabled(checked: Boolean) {
        checked putIn KEY_NETWORK_LOGGING_ENABLED
    }

    override fun getRequestsLoggingEnabled() = KEY_NETWORK_LOGGING_ENABLED getOrElse false

    override fun dropTokens() {
        preferences.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
        }
    }

    override fun setDevToken(token: String) {
        token putIn KEY_DEVELOPMENT_TOKEN
    }

    override fun getDevToken(): String? =
        KEY_DEVELOPMENT_TOKEN getOrElse null

    private infix fun String.putIn(fieldKey: String) {
        preferences.edit()
            .putString(fieldKey, this)
            .apply()
    }

    private infix fun Boolean.putIn(fieldKey: String) {
        preferences.edit()
            .putBoolean(fieldKey, this)
            .apply()
    }

    private infix fun String.getOrElse(default: String?): String? {
        return preferences.getString(this, default)
    }

    private infix fun String.getOrElse(default: Boolean): Boolean {
        return preferences.getBoolean(this, default)
    }
}
