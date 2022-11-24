package uz.ucell.networking.network.request_body

import com.google.gson.annotations.SerializedName

data class PayOrder(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("msisdn")
    val msisdn: String,
)
