package uz.ucell.core_storage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import uz.ucell.core_storage.api.ProfileStorage
import uz.ucell.core_storage.model.DEFAULT_EMPTY_STRING
import uz.ucell.core_storage.model.Profile
import uz.ucell.core_storage.model.dataStore
import javax.inject.Inject

class ProfileStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProfileStorage {

    override suspend fun setUserProfile(profile: Profile) {
        context.dataStore.edit { pref ->
            pref[KEY_PERSONAL_INFO_NAME] = profile.personalInfo.firstName
            pref[KEY_PERSONAL_INFO_SURNAME] = profile.personalInfo.surname
            pref[KEY_PERSONAL_INFO_PATRONYMIC] = profile.personalInfo.patronymic
            pref[KEY_PERSONAL_INFO_BIRTHDAY] = profile.personalInfo.birthday
            pref[KEY_PERSONAL_INFO_GENDER] = profile.personalInfo.gender
            pref[KEY_PERSONAL_INFO_EMAIL] = profile.personalInfo.email
            pref[KEY_ACCOUNT_NUMBER] = profile.accountNumber
            pref[KEY_CONTACT_SINGIN_DATE] = profile.contacSinginDate
            pref[KEY_RATE_PLAN_ID] = profile.ratePlanId
            pref[KEY_SEGMENT] = profile.segment
            pref[KEY_HAS_LINKED_CARDS] = profile.hasLinkedCards
            pref[KEY_LC_STATUS] = profile.lcStatus
        }
    }

    override fun getUserProfile(): Flow<Profile> {
        return context.dataStore.data.map { pref ->
            val personal = getUserPersonalInfo()
            Profile(
                personalInfo = personal.first(),
                accountNumber = pref[KEY_ACCOUNT_NUMBER] ?: DEFAULT_EMPTY_STRING,
                contacSinginDate = pref[KEY_CONTACT_SINGIN_DATE] ?: DEFAULT_EMPTY_STRING,
                ratePlanId = pref[KEY_RATE_PLAN_ID] ?: -1,
                segment = pref[KEY_SEGMENT] ?: DEFAULT_EMPTY_STRING,
                hasLinkedCards = pref[KEY_HAS_LINKED_CARDS] ?: false,
                lcStatus = pref[KEY_LC_STATUS] ?: -1,
            )
        }
    }

    override fun getUserPersonalInfo(): Flow<Profile.PersonalInfo> {
        return context.dataStore.data.map { pref ->
            Profile.PersonalInfo(
                pref[KEY_PERSONAL_INFO_NAME] ?: DEFAULT_EMPTY_STRING,
                pref[KEY_PERSONAL_INFO_SURNAME] ?: DEFAULT_EMPTY_STRING,
                pref[KEY_PERSONAL_INFO_PATRONYMIC] ?: DEFAULT_EMPTY_STRING,
                pref[KEY_PERSONAL_INFO_BIRTHDAY] ?: DEFAULT_EMPTY_STRING,
                pref[KEY_PERSONAL_INFO_GENDER] ?: DEFAULT_EMPTY_STRING,
                pref[KEY_PERSONAL_INFO_EMAIL] ?: DEFAULT_EMPTY_STRING,
            )
        }
    }

    companion object {
        private val KEY_PERSONAL_INFO_NAME = stringPreferencesKey("profile_first_name")
        private val KEY_PERSONAL_INFO_SURNAME = stringPreferencesKey("profile_surname")
        private val KEY_PERSONAL_INFO_PATRONYMIC = stringPreferencesKey("profile_patronymic")
        private val KEY_PERSONAL_INFO_BIRTHDAY = stringPreferencesKey("profile_birthday")
        private val KEY_PERSONAL_INFO_GENDER = stringPreferencesKey("profile_gender")
        private val KEY_PERSONAL_INFO_EMAIL = stringPreferencesKey("profile_email")
        private val KEY_ACCOUNT_NUMBER = stringPreferencesKey("profile_account_number")
        private val KEY_CONTACT_SINGIN_DATE = stringPreferencesKey("profile_contact_sing_date")
        private val KEY_RATE_PLAN_ID = intPreferencesKey("profile_rate_plan_id")
        private val KEY_SEGMENT = stringPreferencesKey("profile_")
        private val KEY_HAS_LINKED_CARDS = booleanPreferencesKey("profile_email")
        private val KEY_LC_STATUS = intPreferencesKey("profile_email")
    }
}
