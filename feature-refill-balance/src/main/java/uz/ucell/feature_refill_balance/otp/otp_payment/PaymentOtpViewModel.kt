package uz.ucell.feature_refill_balance.otp.otp_payment

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
import uz.ucell.networking.network.request_body.Pay
import uz.ucell.utils.timerFlow
import javax.inject.Inject

@HiltViewModel
class PaymentOtpViewModel @Inject constructor(
    private val api: NetworkingInterface,
) : ContainerHost<OtpState, PaymentOtpSideEffect>,
    ViewModel() {

    private var cardId: String = ""
    private var orderId: String = ""

    override val container = container<OtpState, PaymentOtpSideEffect>(OtpState())

    fun setInitialState(timeout: Long, cardId: String, orderId: String) {
        this.cardId = cardId
        this.orderId = orderId

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
            pay(enteredCode)
        }
    }

    private fun pay(enteredCode: String) = intent {
        loading(true)
        val body = Pay(
            cardId = cardId,
            orderId = orderId,
            otp = enteredCode
        )
        when (val response = api.pay(body)) {
            is ApiResponse.Success -> {
                postSideEffect(PaymentOtpSideEffect.ShowSuccessPaymentScreen(response.value.receipt))
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
        when (val response = api.paymentOtpRequest(scenario = PaymentOtpScenario.PAY_BY_CARD)) {
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

    private suspend fun SimpleSyntax<OtpState, PaymentOtpSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<OtpState, PaymentOtpSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            else -> {
                postSideEffect(PaymentOtpSideEffect.ShowError(error.message, error.code))
            }
        }
    }
}
