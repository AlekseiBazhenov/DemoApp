package uz.ucell.networking.network.request_body

import com.google.gson.annotations.SerializedName

data class AddNewCard(
    @SerializedName("number")
    val cardPan: String,
    @SerializedName("expire")
    val expire: String,
    @SerializedName("otp")
    val otp: String? = null,
)
