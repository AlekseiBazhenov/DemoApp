package uz.ucell.feature_credentials.pin_create.contract

sealed class PinCreationSideEffect {
    data class ShowError(
        val text: String,
        val code: String
    ) : PinCreationSideEffect()

    data class OpenPinConfirmationScreen(val code: String) : PinCreationSideEffect()

    object SoftPinWarning : PinCreationSideEffect()
    object PinError : PinCreationSideEffect()
}
