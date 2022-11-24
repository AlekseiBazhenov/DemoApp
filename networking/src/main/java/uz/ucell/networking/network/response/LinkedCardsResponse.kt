package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class LinkedCardsResponse(
    @SerializedName("cards")
    val cards: List<Card> = arrayListOf()
) {
    data class Card(
        @SerializedName("id")
        val id: String,
        @SerializedName("availible")
        val available: Boolean,
        @SerializedName("balance")
        val balance: Double,
        @SerializedName("maskedPan")
        val maskedPan: String,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("otpStatus")
        val otpStatus: Int,
        @SerializedName("linkedDate")
        val linkedDate: String
    )
}
