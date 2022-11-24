package uz.ucell.core_storage.api

import kotlinx.coroutines.flow.Flow
import uz.ucell.core_storage.model.Profile

interface CoreStorage {

    suspend fun saveSelectedLanguage(language: String)
    fun getSelectedLanguage(): Flow<String>

    suspend fun saveMsisdn(msisdn: String)
    fun getMsisdn(): Flow<String>

    fun saveTokenExpireTime(timerange: Long): Nothing {
        TODO("should be implemented")
    }

    fun getTokenExpireTimeObservable(): Nothing {
        TODO("should be implemented")
    }

    suspend fun setDeviceId(deviceId: String)
    fun getDeviceId(): Flow<String>

    suspend fun setUserAgent(userAgent: String)
    fun getUserAgent(): Flow<String>

    suspend fun setPinCreated(created: Boolean)
    fun getPinCreated(): Flow<Boolean>

    suspend fun setUseBiometry(flag: Boolean)
    fun getUseBiometry(): Flow<Boolean>

    suspend fun setBiometryToken(token: String)
    fun getBiometryToken(): Flow<String>

    suspend fun setUserProfile(profile: Profile)
    fun getUserProfile(): Flow<Profile>

    fun getUserPersonalInfo(): Flow<Profile.PersonalInfo>

    suspend fun dropUserData()
}
