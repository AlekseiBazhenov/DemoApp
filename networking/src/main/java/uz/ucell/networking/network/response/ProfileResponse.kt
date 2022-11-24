package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("individualInfo")
    val personal: PersonalInfo,
    @SerializedName("accountNumber")
    val accountNumber: String?,
    @SerializedName("contactSigningDate")
    val contactSinginDate: String?,
    @SerializedName("ratePlanId")
    val ratePlanId: Int,
    @SerializedName("segment")
    val segment: String?,
    @SerializedName("hasLinkedCards")
    val hasLinkedCards: Boolean,
    @SerializedName("lcStatus")
    val lcStatus: Int
)

data class PersonalInfo(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("surname")
    val surname: String?,
    @SerializedName("patronymic")
    val patronymic: String?,
    @SerializedName("birthDate")
    val birthday: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("email")
    val email: String?,
)
