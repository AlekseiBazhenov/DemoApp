package uz.ucell.feature_credentials.pin_confirm.contract

// todo Подумать, возможно использовать PinCreationState.
//  При реализации входа по пину, если будет такой же state
data class PinConfirmationState(
    val isLoading: Boolean = false,
    val pinInput: String = "",
    val pinError: Boolean = false,
)
