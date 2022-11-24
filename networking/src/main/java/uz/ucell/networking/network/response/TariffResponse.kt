package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class TariffListResponse(
    @SerializedName("ratePlans")
    val ratePlans: List<TariffResponse>
)

data class TariffResponse(
    @SerializedName("ratePlanId")
    val ratePlanId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("descr")
    val tariffDescription: String?,
    @SerializedName("nextChargeDate")
    val nextChargeDate: String?,
    @SerializedName("archive")
    val archive: Boolean,
    @SerializedName("ratePlanPrice")
    val ratePlanPrice: RatePlanPrice?,
    @SerializedName("includedLimits")
    val includedLimits: List<IncludedLimit>?,
    @SerializedName("pricesOverLimit")
    val pricesOverLimit: PricesOverLimit,
    @SerializedName("internationalСommunication")
    val internationalCommunication: InternationalCommunication?,
    @SerializedName("faq")
    val faq: List<Faq>?,
    @SerializedName("externalInfo")
    val externalInfo: ExternalInfo?,
    @SerializedName("isFinancialBlock")
    val isFinancialBlock: Boolean,
    @SerializedName("badge")
    val badge: Badge?,
    @SerializedName("advantages")
    val advantages: List<Advantage>
) {
    data class RatePlanPrice(
        @SerializedName("value")
        val value: Int,
        @SerializedName("unit")
        val unit: String,
        @SerializedName("unitPeriod")
        val unitPeriod: String
    )

    data class IncludedLimit(
        @SerializedName("name")
        val name: String,
        @SerializedName("descr")
        val limitDescription: String,
        @SerializedName("isHiddenDescr")
        val isHiddenDescription: Boolean,
        @SerializedName("availableValue")
        val availableValue: Int,
        @SerializedName("totalValue")
        val totalValue: Int,
        @SerializedName("unit")
        val unit: String,
        @SerializedName("isUnlim")
        val isUnlimited: Boolean,
        // @SerializedName("limitType")
        // val limitType: String
    )

    data class PricesOverLimit(
        @SerializedName("prices")
        val prices: List<AdditionalTariffInfo>,
        @SerializedName("isHidden")
        val isHidden: Boolean
    )

    data class InternationalCommunication(
        @SerializedName("communications")
        val communications: List<AdditionalTariffInfo>,
        @SerializedName("isHidden")
        val isHidden: Boolean
    )

    data class AdditionalTariffInfo(
        @SerializedName("name")
        val name: String,
        @SerializedName("value")
        val value: String,
        @SerializedName("descr")
        val description: String?,
        @SerializedName("isHiddenDescr")
        val isHiddenDescription: Boolean
    )

    data class Faq(
        @SerializedName("question")
        val question: String,
        @SerializedName("answer")
        val answer: String
    )

    data class ExternalInfo(
        @SerializedName("pdfUrl")
        val pdfUrl: String?,
        @SerializedName("websiteLink")
        val websiteLink: String?
    )

    data class Badge(
        @SerializedName("title")
        val title: String,
        @SerializedName("color")
        val color: String,
    )

    data class Advantage(
        @SerializedName("name")
        val name: String,
        @SerializedName("icon")
        val icon: String,
    )
}
