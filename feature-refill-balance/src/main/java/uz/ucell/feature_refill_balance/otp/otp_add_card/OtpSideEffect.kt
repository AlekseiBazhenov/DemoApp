package uz.ucell.feature_refill_balance.otp.otp_add_card

sealed class OtpSideEffect {

    object CardBound : OtpSideEffect()

    data class ShowError(
        val text: String,
        val code: String
    ) : OtpSideEffect()

    data class ShowOptError(
        val text: String,
    ) : OtpSideEffect()
}
