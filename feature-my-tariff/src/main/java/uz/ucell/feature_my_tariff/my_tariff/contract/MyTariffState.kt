package uz.ucell.feature_my_tariff.my_tariff.contract

import uz.ucell.feature_my_tariff.my_tariff.views.Faq
import uz.ucell.feature_my_tariff.my_tariff.views.TariffInfoData
import uz.ucell.feature_my_tariff.my_tariff.views.TariffLimit
import uz.ucell.feature_my_tariff.my_tariff.views.TariffTerm

data class MyTariffState(
    val isLoading: Boolean = false,
    val isServerError: Boolean = false,
    val isFinancialBlock: Boolean = false,
    val tariffInfo: TariffInfoData? = null,
    val tariffLimits: List<TariffLimit>? = null,
    val pricesOverLimit: TariffTerm? = null,
    val internationalCommunication: TariffTerm? = null,
    val faq: List<Faq>? = null,
    val tariffInfoLink: String = "",
)
