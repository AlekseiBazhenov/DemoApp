package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class PaymentRecommendationsResponse(
    @SerializedName("main")
    val main: PaymentAmount,

    @SerializedName("additional")
    var additional: List<PaymentAmount>
) {
    data class PaymentAmount(
        @SerializedName("amount")
        val amount: Int
    )
}
