package uz.ucell.networking.network.request_body

import com.google.gson.annotations.SerializedName

data class Pay(
    @SerializedName("cardId")
    val cardId: String,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("otp")
    val otp: String? = null,
)
