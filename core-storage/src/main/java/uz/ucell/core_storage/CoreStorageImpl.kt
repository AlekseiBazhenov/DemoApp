package uz.ucell.core_storage

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.core_storage.api.ProfileStorage
import uz.ucell.core_storage.model.DEFAULT_EMPTY_STRING
import uz.ucell.core_storage.model.Profile
import uz.ucell.core_storage.model.dataStore
import uz.ucell.utils.PhoneFormatter
import javax.inject.Inject

class CoreStorageImpl @Inject constructor(
    private val profileStorage: ProfileStorage,
    @ApplicationContext private val context: Context
) : CoreStorage {

    private fun getStringData(key: Preferences.Key<String>) =
        context.dataStore.data
            .map { preferences ->
                preferences[key] ?: DEFAULT_EMPTY_STRING
            }

    private fun getBooleanData(key: Preferences.Key<Boolean>) =
        context.dataStore.data
            .map { preferences ->
                preferences[key] ?: false
            }

    override suspend fun saveSelectedLanguage(language: String) {
        context.dataStore.edit {
            it[KEY_LANGUAGE] = language
        }
    }

    override fun getSelectedLanguage(): Flow<String> = getStringData(KEY_LANGUAGE)

    override suspend fun saveMsisdn(msisdn: String) {
        context.dataStore.edit {
            it[KEY_MSISDN] = PhoneFormatter.formatPhoneToNumeric(msisdn)
        }
    }

    override fun getMsisdn(): Flow<String> = getStringData(KEY_MSISDN)

    override suspend fun setDeviceId(deviceId: String) {
        context.dataStore.edit {
            it[KEY_DEVICE_ID] = deviceId
        }
    }

    override fun getDeviceId(): Flow<String> = getStringData(KEY_DEVICE_ID)

    override suspend fun setUserAgent(userAgent: String) {
        context.dataStore.edit {
            it[KEY_USER_AGENT] = userAgent
        }
    }

    override fun getUserAgent(): Flow<String> = getStringData(KEY_USER_AGENT)

    override suspend fun setPinCreated(created: Boolean) {
        context.dataStore.edit {
            it[KEY_PIN_CREATED] = created
        }
    }

    override fun getPinCreated(): Flow<Boolean> = getBooleanData(KEY_PIN_CREATED)

    override suspend fun setUseBiometry(flag: Boolean) {
        context.dataStore.edit {
            it[KEY_USE_BIOMETRY] = flag
        }
    }

    override fun getUseBiometry(): Flow<Boolean> = getBooleanData(KEY_USE_BIOMETRY)

    override suspend fun setBiometryToken(token: String) {
        context.dataStore.edit {
            it[KEY_BIOMETRY_TOKEN] = token
        }
    }

    override fun getBiometryToken(): Flow<String> = getStringData(KEY_BIOMETRY_TOKEN)

    override suspend fun setUserProfile(profile: Profile) = profileStorage.setUserProfile(profile)

    override fun getUserProfile(): Flow<Profile> = profileStorage.getUserProfile()

    override fun getUserPersonalInfo(): Flow<Profile.PersonalInfo> =
        profileStorage.getUserPersonalInfo()

    override suspend fun dropUserData() {
        context.dataStore.edit {
            it.remove(KEY_MSISDN)
            it.remove(KEY_PIN_CREATED)
            it.remove(KEY_USE_BIOMETRY)
        }
    }

    companion object {
        @VisibleForTesting
        val KEY_LANGUAGE = stringPreferencesKey("language")

        @VisibleForTesting
        val KEY_MSISDN = stringPreferencesKey("msisdn")
        val KEY_DEVICE_ID = stringPreferencesKey("device_id")
        val KEY_USER_AGENT = stringPreferencesKey("user_agent")
        val KEY_PIN_CREATED = booleanPreferencesKey("pin_created")
        val KEY_USE_BIOMETRY = booleanPreferencesKey("use_biometry")
        val KEY_BIOMETRY_TOKEN = stringPreferencesKey("biometry_token")
    }
}
