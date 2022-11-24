package uz.ucell.networking.data

import com.google.gson.annotations.SerializedName
import uz.ucell.networking.exceptions.B2B_CLIENT
import uz.ucell.networking.exceptions.CONTRACT
import uz.ucell.networking.exceptions.DOCS
import uz.ucell.networking.exceptions.FINANCIAL_BLOCKING
import uz.ucell.networking.exceptions.FRAUD
import uz.ucell.networking.exceptions.FREZZ
import uz.ucell.networking.exceptions.MANUAL
import uz.ucell.networking.exceptions.MNP
import uz.ucell.networking.exceptions.OUT
import uz.ucell.networking.exceptions.PREDISCONNECT
import uz.ucell.networking.exceptions.SCRATCH

data class ErrorBody(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
) {
    val authorizationDenied = when (code) {
        B2B_CLIENT -> true
        FRAUD -> true
        FREZZ -> true
        FINANCIAL_BLOCKING -> true
        SCRATCH -> true
        DOCS -> true
        MANUAL -> true
        MNP -> true
        CONTRACT -> true
        OUT -> true
        PREDISCONNECT -> true
        else -> false
    }
}
