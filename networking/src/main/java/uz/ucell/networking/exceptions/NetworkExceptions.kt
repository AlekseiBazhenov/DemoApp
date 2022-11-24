package uz.ucell.networking.exceptions

sealed class NetworkException @JvmOverloads constructor(
    val code: String,
    errorMessage: String? = null
) : Throwable(errorMessage)

data class RefreshTokenExpired(val ucellCode: String, var ucellMessage: String? = null) :
    NetworkException(ucellCode, ucellMessage)
