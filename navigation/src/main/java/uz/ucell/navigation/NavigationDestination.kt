package uz.ucell.navigation

sealed interface NavigationDestination

object DebugPanel : NavigationDestination

object NetworkingLogsPanel : NavigationDestination

// TODO: убрать null после добавления аналитики в экраны, которые могут вызвать FullscreenError
data class FullscreenError(
    val title: String?,
    val message: String,
    val button: String,
) : NavigationDestination

object SelectLanguage : NavigationDestination

sealed class Authorization : NavigationDestination {
    object ToEnterPhoneFromInitialization : Authorization()
    object ToEnterPhoneFromSelectLanguage : Authorization()
    object ToEnterPhoneFromPinCode : Authorization()

    data class ToSmsCodeFromEnterPhone(
        val phone: String,
        val timeout: Long = 0,
        val showCaptcha: Boolean = false
    ) : Authorization()

    object ToPinCreateFromSmsCode : Authorization()
    data class ToPinConfirmFromPinCreate(val code: String) : Authorization()
    object ToPinCodeFromInitialization : Authorization()
}

sealed class Main : NavigationDestination {
    object ToMainFromPinConfirmation : Main()
    object ToMainFromPinCode : Main()
}

sealed class Refill : NavigationDestination {
    object ToRefillBalanceFromMain : Tariffs()
    data class ToSuccessPaymentFromRefill(
        val receiptId: String,
        val amount: String,
        val commission: String,
        val payDate: String,
        val cardInfo: String,
        val account: String
    ) : Tariffs()
}

sealed class Tariffs : NavigationDestination {
    object ToMyTariffFromMain : Tariffs()
    object ToTariffListFromMyTariff : Tariffs()
}
