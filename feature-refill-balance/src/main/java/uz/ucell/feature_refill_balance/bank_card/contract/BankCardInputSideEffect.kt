package uz.ucell.feature_refill_balance.bank_card.contract

sealed class BankCardInputSideEffect {
    object ReplaceFocusToYear : BankCardInputSideEffect()

    data class CardBound(val cardNumberLastDigits: String) : BankCardInputSideEffect()

    data class ShowOpt(
        val timeout: Long,
        val card: String,
        val expires: String
    ) : BankCardInputSideEffect()

    data class ShowError(
        val text: String,
        val code: String
    ) : BankCardInputSideEffect()
}
