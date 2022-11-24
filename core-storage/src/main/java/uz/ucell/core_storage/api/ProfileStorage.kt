package uz.ucell.core_storage.api

import kotlinx.coroutines.flow.Flow
import uz.ucell.core_storage.model.Profile

interface ProfileStorage {
    suspend fun setUserProfile(profile: Profile)
    fun getUserProfile(): Flow<Profile>

    fun getUserPersonalInfo(): Flow<Profile.PersonalInfo>
}
