package uz.ucell.core_storage.model

data class Profile(
    val personalInfo: PersonalInfo,
    val accountNumber: String,
    val contacSinginDate: String,
    val ratePlanId: Int,
    val segment: String,
    val hasLinkedCards: Boolean,
    val lcStatus: Int,
) {
    data class PersonalInfo(
        val firstName: String,
        val surname: String,
        val patronymic: String,
        val birthday: String,
        val gender: String,
        val email: String
    )
}
