package uz.ucell.feature_credentials.pin_create.contract

data class PinCreationState(
    val isLoading: Boolean = false,
    val pinInput: String = "",
    val pinError: Boolean = false,
)
