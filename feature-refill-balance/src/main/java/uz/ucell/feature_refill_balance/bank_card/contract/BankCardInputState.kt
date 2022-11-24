package uz.ucell.feature_refill_balance.bank_card.contract

import uz.ucell.feature_refill_balance.bank_card.PaymentSystemsAll

data class BankCardInputState(
    val isLoading: Boolean = false,
    val card: String = "",
    val month: String = "",
    val year: String = "",
    val cardPaymentSystem: PaymentSystemsAll? = null
) {
    fun getExpire(): String {
        return "$year$month"
    }
}
