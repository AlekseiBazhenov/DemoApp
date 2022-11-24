package uz.ucell.feature_refill_balance.payment_confirmation.contract

import uz.ucell.networking.network.response.PayResponse

sealed class ConfirmationSideEffect {

    data class ShowOtp(
        val timeout: Long,
        val orderId: String
    ) : ConfirmationSideEffect()

    data class OpenSuccessPayment(
        val receipt: PayResponse.Receipt,
    ) : ConfirmationSideEffect()

    data class ShowError(
        val text: String,
        val code: String
    ) : ConfirmationSideEffect()

    data class ShowOptError(
        val text: String,
    ) : ConfirmationSideEffect()
}
