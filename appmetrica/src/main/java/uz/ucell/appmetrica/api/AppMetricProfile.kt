package uz.ucell.appmetrica.api

import com.yandex.metrica.profile.GenderAttribute
import uz.ucell.appmetrica.model.UserType

interface AppMetricProfile {
    /**
     * Send primary [id], [gender], [age] and custom [ratePlanId], [lang], [auth],
     * [linkedCard], [userType], [push], [statusId] metric in AppMetrica user profile
     */
    fun sendProfileMetric(
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
    )
}
