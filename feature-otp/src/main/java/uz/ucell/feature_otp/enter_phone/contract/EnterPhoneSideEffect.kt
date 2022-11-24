package uz.ucell.feature_otp.enter_phone.contract

sealed class EnterPhoneSideEffect {
    data class ShowError(val text: String, val code: String) : EnterPhoneSideEffect()
    data class ShowAuthDeniedError(val text: String, val code: String) : EnterPhoneSideEffect()

    data class OpenOffer(val link: String) : EnterPhoneSideEffect()

    data class OpenSmsCode(
        val phoneNumber: String,
        val timeout: Long
    ) : EnterPhoneSideEffect()

    data class OpenSmsCodeWithCaptcha(
        val phoneNumber: String,
    ) : EnterPhoneSideEffect()

    data class ChangeLanguage(val newLanguage: String) : EnterPhoneSideEffect()
}
