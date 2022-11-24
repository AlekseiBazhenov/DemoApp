package uz.ucell.feature_refill_balance.contract

sealed class RefillBalanceSideEffect {
    data class ShowError(
        val text: String,
        val code: String
    ) : RefillBalanceSideEffect()
}
