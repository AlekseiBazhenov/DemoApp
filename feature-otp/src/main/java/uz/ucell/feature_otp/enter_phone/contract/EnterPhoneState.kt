package uz.ucell.feature_otp.enter_phone.contract

import uz.ucell.core.models.Language

data class EnterPhoneState(
    val phoneInput: String = "",
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val selectedLanguage: Language? = null,
    val hasPhoneError: Boolean = false,
    val phoneErrorMessage: String = "",
)
