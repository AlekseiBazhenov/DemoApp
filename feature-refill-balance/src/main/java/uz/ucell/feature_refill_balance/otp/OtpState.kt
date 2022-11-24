package uz.ucell.feature_refill_balance.otp

data class OtpState(
    val isLoading: Boolean = false,
    val codeInput: String = "",
    val hasCodeInputError: Boolean = false,
    val codeInputError: String = "",
    val timeoutSeconds: Long = 0,
    val isTimerFinished: Boolean = true,
)
