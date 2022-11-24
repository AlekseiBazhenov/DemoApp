package uz.ucell.debugpanel.presentation.contract

data class EnterDebugPanelState(
    val host: String = "",
    val deviceId: String = "",
    val userAgent: String = "",
    val msisdn: String = "",
    val networkLoggingEnabled: Boolean = false,
    val devToken: String = ""
)
