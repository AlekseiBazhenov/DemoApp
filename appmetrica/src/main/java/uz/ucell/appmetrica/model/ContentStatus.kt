package uz.ucell.appmetrica.model

sealed class ContentStatus(val status: String) {
    object Loading : ContentStatus(LOADING_STRING_VALUE)
    object Available : ContentStatus(AVAILABLE_STRING_VALUE)
    data class Error(val errorCode: String) : ContentStatus(ERROR_STRING_VALUE)
}

private const val LOADING_STRING_VALUE: String = "loading"
private const val AVAILABLE_STRING_VALUE: String = "available"
private const val ERROR_STRING_VALUE: String = "error"
