package uz.ucell.networking.network.response

import com.google.gson.annotations.SerializedName

data class PayResponse(
    @SerializedName("receipt")
    val receipt: Receipt
) {
    data class Receipt(
        @SerializedName("id")
        val id: String,
        @SerializedName("payDate")
        val payDate: String,
        @SerializedName("maskedPan")
        val maskedPan: String,
        @SerializedName("merchant")
        val merchant: Merchant,
        @SerializedName("accounts")
        val accounts: List<Account>,
        @SerializedName("amount")
        val amount: Double,
        @SerializedName("commission")
        val commission: Double
    )

    data class Account(
        @SerializedName("field")
        val field: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("value")
        val value: String
    )

    data class Merchant(
        @SerializedName("name")
        val name: String,
        @SerializedName("organization")
        val organization: String,
        @SerializedName("logo")
        val logo: String
    )
}
