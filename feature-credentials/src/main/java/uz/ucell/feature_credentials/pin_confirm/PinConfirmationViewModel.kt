package uz.ucell.feature_credentials.pin_confirm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.appmetrica.api.useCase.PinConfAnalyticsUseCase
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core.constatns.PIN_LENGTH
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.feature_credentials.pin_confirm.contract.PinConfirmationSideEffect
import uz.ucell.feature_credentials.pin_confirm.contract.PinConfirmationState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.network.request_body.BiometryToken
import uz.ucell.networking.network.request_body.PinCode
import uz.ucell.utils.PhoneUtils
import java.util.Base64
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PinConfirmationViewModel @Inject constructor(
    private val coreStorage: CoreStorage,
    private val api: NetworkingInterface,
    private val phoneUtils: PhoneUtils,
    private val analyticsUseCase: PinConfAnalyticsUseCase
) : ContainerHost<PinConfirmationState, PinConfirmationSideEffect>, ViewModel() {

    override val container =
        container<PinConfirmationState, PinConfirmationSideEffect>(PinConfirmationState())

    fun onScreenEvent() = intent {
        analyticsUseCase.sendScreenMetric(ContentStatus.Available)
    }

    fun onPinInputChanged(pinDigit: String, enteredPin: String) = intent {
        reduce {
            state.copy(pinInput = state.pinInput.plus(pinDigit))
        }

        if (state.pinInput.length == PIN_LENGTH) {
            val clickMetricValue: String
            if (state.pinInput == enteredPin) {
                createPin()
                analyticsUseCase.sendClickPinRepeat(true)
            } else {
                analyticsUseCase.sendClickPinRepeat(false)
                reduce {
                    state.copy(pinError = true)
                }
                postSideEffect(PinConfirmationSideEffect.PinError)
                delay(500)
                postSideEffect(PinConfirmationSideEffect.OpenPreviousScreen)
            }
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

    private fun createPin() = intent {
        loading(true)
        when (val response = api.createPinCode(PinCode(state.pinInput))) {
            is ApiResponse.Success -> {
                coreStorage.setPinCreated(true)
                if (phoneUtils.isBiometricReady()) {
                    postSideEffect(PinConfirmationSideEffect.OpenBiometryScreen)
                } else {
                    analyticsUseCase.sendConversionAuth()
                    postSideEffect(PinConfirmationSideEffect.OpenMyTariffScreen)
                }
            }
            is ApiResponse.Error -> {
                response.error?.let { showErrorDialog(it) }
            }
        }
        loading(false)
    }

    fun onBiometricAuthenticationSuccess() = intent {
        loading(true)
        val bytes = UUID.randomUUID().toString().toByteArray()
        val json = Base64.getEncoder().encodeToString(bytes)
        when (val response = api.createBiometryToken(BiometryToken(json))) {
            is ApiResponse.Success -> {
                publishSuccessBiometryEvent()
                coreStorage.setBiometryToken(json)
                coreStorage.setUseBiometry(true)
                postSideEffect(PinConfirmationSideEffect.OpenMyTariffScreen)
            }
            is ApiResponse.Error -> {
                response.error?.let {
                    publishFailureBiometryEvent()
                    postSideEffect(PinConfirmationSideEffect.ShowBiometryError)
                }
            }
        }
        loading(false)
    }

    fun publishFailureBiometryEvent() = intent {
        analyticsUseCase.sendClickAuthBiometry(false)
    }

    fun publishSuccessBiometryEvent() = intent {
        analyticsUseCase.sendClickAuthBiometry(true)
    }

    fun onDismissBiometryUsage() = intent {
        coreStorage.setUseBiometry(false)
        publishFailureBiometryEvent()
        postSideEffect(PinConfirmationSideEffect.OpenMyTariffScreen)
    }

    fun onAuthBiometryDialogEvent() = intent {
        analyticsUseCase.sendModalAuthBiometry()
    }

    private suspend fun SimpleSyntax<PinConfirmationState, PinConfirmationSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<PinConfirmationState, PinConfirmationSideEffect>.showErrorDialog(
        error: ErrorBody
    ) {
        postSideEffect(PinConfirmationSideEffect.ShowError(error.message, error.code))
    }
}
