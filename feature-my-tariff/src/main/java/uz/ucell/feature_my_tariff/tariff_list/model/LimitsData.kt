package uz.ucell.feature_my_tariff.tariff_list.model

import uz.ucell.ui_kit.R

enum class LimitType(val icon: Int, val value: String) {
    INTERNET(R.drawable.mobile_internet_16, INTERNET_STRING_VALUE),
    CALLS(R.drawable.call_16, CALLS_STRING_VALUE),
    SMS(R.drawable.message_16, SMS_STRING_VALUE)
}

sealed class TariffLimit(val icon: Int, val limitStringValue: String) {
    data class Limited(
        val value: Int,
        val unit: String,
        val limitType: LimitType
    ) : TariffLimit(limitType.icon, "$value $unit")

    data class Unlimited(
        val unlimitedStringValue: String,
        val limitType: LimitType
    ) : TariffLimit(limitType.icon, unlimitedStringValue)
}

private const val INTERNET_STRING_VALUE: String = "Internet"
private const val CALLS_STRING_VALUE: String = "Calls"
private const val SMS_STRING_VALUE: String = "SMS"
