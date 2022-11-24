package uz.ucell.feature_authorization.pin_auth.contract

sealed class PinAuthSideEffect {
    data class ShowError(
        val text: String,
        val code: String
    ) : PinAuthSideEffect()

    data class ShowNeedAuthAgain(
        val text: String,
    ) : PinAuthSideEffect()

    data class ShowAuthDeniedError(val text: String, val code: String) : PinAuthSideEffect()

    object PinError : PinAuthSideEffect()

    object OpenBiometryScreen : PinAuthSideEffect()
    object OpenMyTariffScreen : PinAuthSideEffect()
    object OpenEnterPhoneScreen : PinAuthSideEffect()
}
