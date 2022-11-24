package uz.ucell.feature_my_tariff.tariff_list.model

import uz.ucell.feature_my_tariff.tariff_list.ui.TariffCell

data class TariffListState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val tariffCells: List<TariffCell> = listOf()
)
