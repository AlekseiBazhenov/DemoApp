package uz.ucell.feature_my_tariff.my_tariff.contract

sealed class MyTariffSideEffect {
    data class ShowError(
        val text: String,
        val code: String
    ) : MyTariffSideEffect()
}
