package uz.ucell.appmetrica.model

internal enum class EventType(val eventName: String) {
    SCREEN_VIEW(SCREEN_VIEW_STRING_VALUE),
    SCREEN_PARAMS(SCREEN_PARAMS_STRING_VALUE),
    CLICK(CLICK_STRING_VALUE),
    CONVERSION(CONVERSION_STRING_VALUE),
}

private const val SCREEN_VIEW_STRING_VALUE: String = "screenview"
private const val SCREEN_PARAMS_STRING_VALUE: String = "screenparams"
private const val CLICK_STRING_VALUE: String = "click"
private const val CONVERSION_STRING_VALUE: String = "conversion"
