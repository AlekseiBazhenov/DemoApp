package uz.ucell.feature_credentials.pin_confirm.contract

sealed class PinConfirmationSideEffect {
    data class ShowError(
        val text: String,
        val code: String
    ) : PinConfirmationSideEffect()

    object PinError : PinConfirmationSideEffect()
    object OpenPreviousScreen : PinConfirmationSideEffect()
    object OpenBiometryScreen : PinConfirmationSideEffect()
    object OpenMyTariffScreen : PinConfirmationSideEffect()
    object ShowBiometryError : PinConfirmationSideEffect()
}
