package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class PaymentLimitsResponse(
    @SerializedName("minSum")
    val minSum: Int,
    @SerializedName("maxSum")
    val maxSum: Int,
    @SerializedName("commission")
    val commission: String,
)
