package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class BalanceResponse(
    @SerializedName("balance")
    val balance: Double,
)
