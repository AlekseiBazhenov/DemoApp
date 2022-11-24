package uz.ucell.feature_refill_balance.selected_card.contract

sealed class SelectedCardSideEffect {

    data class ShowError(
        val text: String,
        val code: String
    ) : SelectedCardSideEffect()

    data class OpenPaymentConfirmation(
        val orderId: String,
        val commission: Double
    ) : SelectedCardSideEffect()
}
