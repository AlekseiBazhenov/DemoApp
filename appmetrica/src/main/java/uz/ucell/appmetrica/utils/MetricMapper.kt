package uz.ucell.appmetrica.utils

import com.yandex.metrica.profile.GenderAttribute
import dagger.hilt.android.scopes.ViewModelScoped
import uz.ucell.appmetrica.model.UserType
import uz.ucell.utils.SERVER_SIMPLE_DATE_TIME_FORMAT
import uz.ucell.utils.ZERO
import uz.ucell.utils.calculateAgeFrom
import javax.inject.Inject

@ViewModelScoped
class MetricMapper @Inject constructor() {
    fun mapGender(gender: String?): GenderAttribute.Gender {
        return when (gender?.lowercase()) {
            "male" -> GenderAttribute.Gender.MALE
            "female" -> GenderAttribute.Gender.FEMALE
            else -> GenderAttribute.Gender.OTHER
        }
    }

    fun mapUserType(segment: String?): UserType {
        return UserType.values().find { it.type == segment?.lowercase() } ?: UserType.NONE
    }

    fun mapAge(birthday: String?): Int {
        return try {
            calculateAgeFrom(birthday!!, SERVER_SIMPLE_DATE_TIME_FORMAT)
        } catch (e: Exception) {
            ZERO
        }
    }
}
