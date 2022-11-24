package uz.ucell.feature_refill_balance.payment_confirmation.contract

data class ConfirmationState(
    val isLoading: Boolean = false,
    val phoneNumber: String = "",
    val card: String = "",
    val amount: String = "",
    val commission: String = ""
)
