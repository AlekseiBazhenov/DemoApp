package uz.ucell.networking.network.request_body

import com.google.gson.annotations.SerializedName

data class CheckNewCard(
    @SerializedName("number")
    val number: String,
    @SerializedName("expire")
    val expire: String,
)
