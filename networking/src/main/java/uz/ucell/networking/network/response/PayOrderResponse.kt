package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class PayOrderResponse(
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("commission")
    val commission: Double,
)
