package uz.ucell.feature_authorization.pin_auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.appmetrica.api.useCase.AuthAnalyticsUseCase
import uz.ucell.appmetrica.api.useCase.SendProfileMetricUseCase
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core.constatns.PIN_LENGTH
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.feature_authorization.pin_auth.contract.PinAuthSideEffect
import uz.ucell.feature_authorization.pin_auth.contract.PinAuthState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.SetupNetworkInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.exceptions.INCORRECT_BIOMETRY_CODE
import uz.ucell.networking.exceptions.INCORRECT_PIN_CODE
import uz.ucell.networking.exceptions.TOO_MUCH_INCORRECT_PIN_AUTH
import uz.ucell.networking.network.PasswordType
import uz.ucell.utils.PhoneUtils
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val coreStorage: CoreStorage,
    private val phoneUtils: PhoneUtils,
    private val api: NetworkingInterface,
    private val netSettings: SetupNetworkInterface,
    private val sendProfileUseCase: SendProfileMetricUseCase,
    private val analyticsUseCase: AuthAnalyticsUseCase
) : ContainerHost<PinAuthState, PinAuthSideEffect>, ViewModel() {

    override val container =
        container<PinAuthState, PinAuthSideEffect>(PinAuthState())

    init {
        intent {
            val useBiometry = coreStorage.getUseBiometry().first() && phoneUtils.isBiometricReady()
            reduce {
                state.copy(showBiometry = useBiometry)
            }
            if (useBiometry) {
                onBiometryDialogEvent()
                postSideEffect(PinAuthSideEffect.OpenBiometryScreen)
            }
        }
    }

    fun onScreenEvent() = intent {
        analyticsUseCase.sendScreenMetric(ContentStatus.Available)
    }

    private suspend fun onBiometryDialogEvent() {
        analyticsUseCase.sendModalLoginBiometry()
    }

    fun onPinInputChanged(pinDigit: String) = intent {
        reduce {
            state.copy(
                pinInput = state.pinInput.plus(pinDigit),
                pinError = false,
                pinErrorMessage = ""
            )
        }

        val input = state.pinInput
        if (input.length == PIN_LENGTH) {
            val phone = coreStorage.getMsisdn().first()
            authorize(phone, state.pinInput, PasswordType.PIN)
        }
    }

    fun onRemoveSymbol() = intent {
        val enteredPin = state.pinInput
        if (enteredPin.isNotEmpty()) {
            reduce {
                state.copy(pinInput = enteredPin.dropLast(1))
            }
        }
    }

    private fun authorize(
        username: String,
        password: String,
        passwordType: PasswordType,
    ) = intent {
        loading(true)
        when (
            val response = api.authorization(
                username = username,
                password = password,
                passwordType = passwordType
            )
        ) {
            is ApiResponse.Success -> {
                sendSuccessLoginEvents(passwordType)
                // TODO сделать автообновление access токена до истечения срока действия
                postSideEffect(PinAuthSideEffect.OpenMyTariffScreen)
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    private suspend fun sendSuccessLoginEvents(passwordType: PasswordType) {
        when (passwordType) {
            PasswordType.BIOMETRY -> {
                analyticsUseCase.sendClickLoginBiometry(true)
                analyticsUseCase.sendConversionBiometryLogin()
            }
            PasswordType.PIN -> {
                analyticsUseCase.sendClickLoginPin(true)
                analyticsUseCase.sendConversionPinLogin()
            }
            else -> {}
        }

        sendProfileUseCase.sendProfileMetric()
    }

    fun onBiometricAuthenticationFailure() = intent {
        analyticsUseCase.sendClickLoginBiometry(false)
    }

    fun onBiometricAuthenticationSuccess() = intent {
        val msisdn = coreStorage.getMsisdn().first()
        val json = coreStorage.getBiometryToken().first()
        authorize(msisdn, json, PasswordType.BIOMETRY)
    }

    private suspend fun SimpleSyntax<PinAuthState, PinAuthSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<PinAuthState, PinAuthSideEffect>.processError(
        error: ErrorBody
    ) {
        reduce {
            state.copy(pinInput = "")
        }

        when (error.code) {
            INCORRECT_BIOMETRY_CODE, TOO_MUCH_INCORRECT_PIN_AUTH -> {
                coreStorage.dropUserData()
                // TODO: add analytics in full screen error
                postSideEffect(PinAuthSideEffect.ShowNeedAuthAgain(error.message,))
            }
            INCORRECT_PIN_CODE -> {
                analyticsUseCase.sendClickLoginPin(false)
                reduce {
                    state.copy(
                        pinError = true,
                        pinErrorMessage = error.message
                    )
                }
            }
            else -> {
                if (error.authorizationDenied) {
                    postSideEffect(
                        PinAuthSideEffect.ShowAuthDeniedError(
                            error.message,
                            error.code
                        )
                    )
                } else {
                    postSideEffect(PinAuthSideEffect.ShowError(error.message, error.code))
                }
            }
        }
    }

    fun onLogoutClick() = intent {
        coreStorage.dropUserData()
        netSettings.dropTokens()
        analyticsUseCase.sendClickLogout()
        postSideEffect(PinAuthSideEffect.OpenEnterPhoneScreen)
        api.logout()
    }
}
