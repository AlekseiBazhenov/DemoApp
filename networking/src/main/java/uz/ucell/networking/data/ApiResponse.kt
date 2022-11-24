package uz.ucell.networking.data

sealed class ApiResponse<out T> {

    data class Success<out T>(val value: T) : ApiResponse<T>()

    data class Error(
        val isNetworkError: Boolean?,
        val error: ErrorBody?,
    ) : ApiResponse<Nothing>()
}
