package uz.ucell.feature_refill_balance.otp.otp_add_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.core.constatns.SMS_CODE_LENGTH
import uz.ucell.feature_refill_balance.otp.OtpState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.network.PaymentOtpScenario
import uz.ucell.networking.network.request_body.AddNewCard
import uz.ucell.utils.timerFlow
import javax.inject.Inject

@HiltViewModel
class AddCardOtpViewModel @Inject constructor(
    private val api: NetworkingInterface,
) : ContainerHost<OtpState, OtpSideEffect>,
    ViewModel() {

    private var card: String = ""
    private var expires: String = ""

    override val container =
        container<OtpState, OtpSideEffect>(OtpState())

    fun setInitialState(timeout: Long, card: String, expires: String) {
        this.card = card
        this.expires = expires

        intent {
            if (state.isTimerFinished && timeout > 0) {
                startTimer(timeout)
            }
        }
    }

    fun onSmsCodeInputChanged(enteredCode: String) = intent {
        reduce {
            state.copy(
                codeInput = enteredCode,
                hasCodeInputError = false
            )
        }
        if (enteredCode.length == SMS_CODE_LENGTH) {
            addNewCard(enteredCode)
        }
    }

    private fun addNewCard(enteredCode: String) = intent {
        loading(true)
        when (val response = api.addNewCard(AddNewCard(card, expires, enteredCode))) {
            is ApiResponse.Success -> {
                postSideEffect(OtpSideEffect.CardBound)
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    fun onRepeatOtpClick() = intent {
        if (!state.isTimerFinished) {
            return@intent
        }

        loading(true)
        when (val response = api.paymentOtpRequest(scenario = PaymentOtpScenario.ADD_NEW_CARD)) {
            is ApiResponse.Success -> {
                reduce {
                    state.copy(
                        hasCodeInputError = false,
                        codeInput = "",
                    )
                }
                startTimer(response.value.timeout)
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startTimer(initialTimeoutSeconds: Long) = intent {
        reduce {
            state.copy(isTimerFinished = false)
        }

        timerFlow(initCountDownSeconds = initialTimeoutSeconds)
            .onEach {
                reduce {
                    state.copy(timeoutSeconds = it)
                }
            }
            .onCompletion {
                reduce {
                    state.copy(isTimerFinished = true)
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun SimpleSyntax<OtpState, OtpSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<OtpState, OtpSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            else -> {
                postSideEffect(OtpSideEffect.ShowError(error.message, error.code))
            }
        }
    }
}
