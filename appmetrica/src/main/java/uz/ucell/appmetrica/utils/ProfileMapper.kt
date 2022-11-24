package uz.ucell.appmetrica.utils

import uz.ucell.core_storage.model.Profile
import uz.ucell.networking.network.response.ProfileResponse
import javax.inject.Inject

private const val EMPTY_STRING: String = ""
// TODO: вынести в core/networking если появятся использования за пределами метрики
class ProfileMapper @Inject constructor() {
    fun mapResponseToDomain(profileResponse: ProfileResponse): Profile {
        val personalInfo: Profile.PersonalInfo = Profile.PersonalInfo(
            profileResponse.personal.firstName ?: EMPTY_STRING,
            profileResponse.personal.surname ?: EMPTY_STRING,
            profileResponse.personal.patronymic ?: EMPTY_STRING,
            profileResponse.personal.birthday ?: EMPTY_STRING,
            profileResponse.personal.gender ?: EMPTY_STRING,
            profileResponse.personal.email ?: EMPTY_STRING,
        )

        return Profile(
            personalInfo = personalInfo,
            accountNumber = profileResponse.accountNumber ?: EMPTY_STRING,
            contacSinginDate = profileResponse.contactSinginDate ?: EMPTY_STRING,
            ratePlanId = profileResponse.ratePlanId,
            segment = profileResponse.segment ?: EMPTY_STRING,
            hasLinkedCards = profileResponse.hasLinkedCards,
            lcStatus = profileResponse.lcStatus
        )
    }
}
