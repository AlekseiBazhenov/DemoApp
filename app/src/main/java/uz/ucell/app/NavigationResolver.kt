package uz.ucell.app

import androidx.navigation.NavDirections
import uz.ucell.debugpanel.presentation.DebugPanelFragmentDirections
import uz.ucell.feature_authorization.pin_auth.AuthFragmentDirections
import uz.ucell.feature_credentials.pin_confirm.PinConfirmationFragmentDirections
import uz.ucell.feature_credentials.pin_create.PinCreationFragmentDirections
import uz.ucell.feature_initialization.InitializationFragmentDirections
import uz.ucell.feature_main_screen.MainScreenFragmentDirections
import uz.ucell.feature_my_tariff.my_tariff.MyTariffFragmentDirections
import uz.ucell.feature_otp.enter_phone.EnterPhoneFragmentDirections
import uz.ucell.feature_otp.sms.SmsCodeFragmentDirections
import uz.ucell.feature_refill_balance.RefillBalanceFragmentDirections
import uz.ucell.feature_select_language.SelectLanguageFragmentDirections
import uz.ucell.navigation.Authorization
import uz.ucell.navigation.DebugPanel
import uz.ucell.navigation.FullscreenError
import uz.ucell.navigation.Main
import uz.ucell.navigation.NavigationDestination
import uz.ucell.navigation.NetworkingLogsPanel
import uz.ucell.navigation.Refill
import uz.ucell.navigation.SelectLanguage
import uz.ucell.navigation.Tariffs

object NavigationResolver {
    fun toDirection(
        destination: NavigationDestination
    ): NavDirections = when (destination) {
        is DebugPanel -> MainNavGraphDirections.actionGlobalDebugpanel()
        is NetworkingLogsPanel -> DebugPanelFragmentDirections.toNetworkLogsPanel()
        is FullscreenError -> MainNavGraphDirections.actionGlobalShowFullscreenError(
            title = destination.title,
            message = destination.message,
            button = destination.button,
        )

        is SelectLanguage -> InitializationFragmentDirections.toSelectLanguage()
        is Authorization.ToEnterPhoneFromInitialization -> InitializationFragmentDirections.toEnterPhoneNumber()
        is Authorization.ToEnterPhoneFromSelectLanguage -> SelectLanguageFragmentDirections.toEnterPhoneNumber()
        is Authorization.ToEnterPhoneFromPinCode -> AuthFragmentDirections.toEnterPhoneNumber()
        is Authorization.ToSmsCodeFromEnterPhone -> EnterPhoneFragmentDirections.toSmsCode(
            phoneNumber = destination.phone,
            timeout = destination.timeout,
            showCaptcha = destination.showCaptcha,
        )
        is Authorization.ToPinCreateFromSmsCode -> SmsCodeFragmentDirections.toPinCode()
        is Authorization.ToPinConfirmFromPinCreate -> PinCreationFragmentDirections.toPinConfirmation(code = destination.code)
        is Authorization.ToPinCodeFromInitialization -> InitializationFragmentDirections.toPinAuth()
        is Main.ToMainFromPinConfirmation -> PinConfirmationFragmentDirections.toMainScreen()
        is Main.ToMainFromPinCode -> AuthFragmentDirections.toMainScreen()
        is Tariffs.ToMyTariffFromMain -> MainScreenFragmentDirections.toMyTariff()
        is Tariffs.ToTariffListFromMyTariff -> MyTariffFragmentDirections.toTariffList()
        is Refill.ToRefillBalanceFromMain -> MainScreenFragmentDirections.toRefillBalance()
        is Refill.ToSuccessPaymentFromRefill -> RefillBalanceFragmentDirections.toSuccessPayment(
            destination.receiptId,
            destination.amount,
            destination.commission,
            destination.payDate,
            destination.cardInfo,
            destination.account
        )
    }
}
