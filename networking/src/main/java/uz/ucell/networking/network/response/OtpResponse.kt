package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class OtpResponse(
    @SerializedName("timeout")
    val timeout: Long,
)
