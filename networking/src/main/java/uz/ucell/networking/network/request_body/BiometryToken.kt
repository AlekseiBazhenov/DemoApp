package uz.ucell.networking.network.request_body

import com.google.gson.annotations.SerializedName

data class BiometryToken(
    @SerializedName("biometryToken")
    val biometryToken: String
)
