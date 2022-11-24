package uz.ucell.appmetrica.api.impl

import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.profile.Attribute
import com.yandex.metrica.profile.GenderAttribute
import com.yandex.metrica.profile.UserProfile
import uz.ucell.appmetrica.api.AppMetricProfile
import uz.ucell.appmetrica.model.UserType

internal class AppMetricProfileImpl : AppMetricProfile {
    override fun sendProfileMetric(
        // id: String,
        gender: GenderAttribute.Gender,
        age: Int,
        ratePlanId: Int,
        lang: String,
        auth: Boolean,
        linkedCard: Boolean,
        userType: UserType,
        push: Boolean,
        statusId: Int
    ) {
        val profile = UserProfile.newBuilder()
            .apply(Attribute.gender().withValue(gender))
            .apply(Attribute.birthDate().withAge(age))
            .apply(Attribute.customNumber(RATE_PLAN_ID).withValue(ratePlanId.toDouble()))
            .apply(Attribute.customString(LANG).withValue(lang))
            .apply(Attribute.customBoolean(AUTH).withValue(auth))
            .apply(Attribute.customBoolean(LINKED_CARD).withValue(linkedCard))
            .apply(Attribute.customString(USER_TYPE).withValue(userType.type))
            .apply(Attribute.customBoolean(PUSH).withValue(push))
            .apply(Attribute.customNumber(STATUS_ID).withValue(statusId.toDouble()))
            .build()

        // YandexMetrica.setUserProfileID(id)
        YandexMetrica.reportUserProfile(profile)
    }

    private companion object {
        const val RATE_PLAN_ID = "ratePlanId"
        const val LANG = "lang"
        const val AUTH = "auth"
        const val LINKED_CARD = "linkedCard"
        const val USER_TYPE = "userType"
        const val PUSH = "push"
        const val STATUS_ID = "statusID"
    }
}
