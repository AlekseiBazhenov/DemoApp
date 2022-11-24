package uz.ucell.feature_credentials.pin_create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.appmetrica.api.useCase.PinCreationAnalyticsUseCase
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core.constatns.PIN_LENGTH
import uz.ucell.feature_credentials.pin_create.contract.PinCreationSideEffect
import uz.ucell.feature_credentials.pin_create.contract.PinCreationState
import javax.inject.Inject

@HiltViewModel
class PinCreationViewModel @Inject constructor(
    private val analyticsUseCase: PinCreationAnalyticsUseCase
) : ContainerHost<PinCreationState, PinCreationSideEffect>, ViewModel() {

    override val container =
        container<PinCreationState, PinCreationSideEffect>(PinCreationState())

    fun onScreenEvent() = intent {
        analyticsUseCase.sendScreenMetric(ContentStatus.Available)
    }

    fun onPinInputChanged(pinDigit: String) = intent {
        reduce {
            state.copy(pinInput = state.pinInput.plus(pinDigit))
        }
    }

    fun checkIsPinValid(pin: String) = intent {
        if (pin.length == PIN_LENGTH) {
            if (isPinStrong(pin)) {
                postSideEffect(PinCreationSideEffect.SoftPinWarning)
            } else {
                openPinConfirmation(pin)
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

    fun onEnterPinAgain() = intent {
        analyticsUseCase.sendClickPinSetupSimpleCodeEnterNew()
        reduce {
            state.copy(pinInput = "")
        }
    }

    fun onContinueWithSoftPin() = intent {
        analyticsUseCase.sendClickPinSetupSimpleCodeContinue()
        openPinConfirmation(state.pinInput)
    }

    private suspend fun SimpleSyntax<PinCreationState, PinCreationSideEffect>.openPinConfirmation(
        input: String
    ) {
        analyticsUseCase.sendClickPinSetup()
        postSideEffect(PinCreationSideEffect.OpenPinConfirmationScreen(input))
        reduce {
            state.copy(pinInput = "")
        }
    }

    // Правила могут быть расширены
    private fun isPinStrong(pin: String) =
        pin.count { it == pin.first() } == PIN_LENGTH
}
