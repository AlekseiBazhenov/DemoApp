package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class CheckNewCardResponse(
    @SerializedName("otpStatus")
    val otpStatus: Int
)
