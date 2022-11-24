package uz.ucell.feature_otp.sms.contract

sealed class EnterSmsCodeSideEffect {
    data class ShowError(
        val text: String,
        val code: String
    ) : EnterSmsCodeSideEffect()

    data class ShowOptError(
        val text: String,
    ) : EnterSmsCodeSideEffect()

    data class ShowAuthDeniedError(val text: String, val code: String) : EnterSmsCodeSideEffect()

    object OpenPinScreen : EnterSmsCodeSideEffect()
}
