package uz.ucell.feature_my_tariff.tariff_list.model

sealed class TariffListSideEffect {
    data class Error(val message: String, val code: String) : TariffListSideEffect()
}
