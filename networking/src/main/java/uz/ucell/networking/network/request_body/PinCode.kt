package uz.ucell.networking.network.request_body

import com.google.gson.annotations.SerializedName

data class PinCode(
    @SerializedName("pin")
    val pin: String
)
