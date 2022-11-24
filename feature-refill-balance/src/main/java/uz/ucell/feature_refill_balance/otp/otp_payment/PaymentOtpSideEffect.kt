package uz.ucell.feature_refill_balance.otp.otp_payment

import uz.ucell.networking.network.response.PayResponse

sealed class PaymentOtpSideEffect {

    data class ShowSuccessPaymentScreen(
        val receipt: PayResponse.Receipt
    ) : PaymentOtpSideEffect()

    data class ShowError(
        val text: String,
        val code: String
    ) : PaymentOtpSideEffect()

    data class ShowOptError(
        val text: String,
    ) : PaymentOtpSideEffect()
}
