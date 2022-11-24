package uz.ucell.feature_authorization.pin_auth.contract

data class PinAuthState(
    val isLoading: Boolean = false,
    val showBiometry: Boolean = false,
    val pinInput: String = "",
    val pinError: Boolean = false,
    val pinErrorMessage: String = "",
)
