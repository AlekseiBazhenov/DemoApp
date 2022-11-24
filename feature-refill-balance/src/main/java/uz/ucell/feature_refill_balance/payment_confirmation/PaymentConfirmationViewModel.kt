package uz.ucell.feature_refill_balance.payment_confirmation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.feature_refill_balance.payment_confirmation.contract.ConfirmationSideEffect
import uz.ucell.feature_refill_balance.payment_confirmation.contract.ConfirmationState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.network.PaymentOtpScenario
import uz.ucell.networking.network.request_body.Pay
import javax.inject.Inject

@HiltViewModel
class PaymentConfirmationViewModel @Inject constructor(
    private val api: NetworkingInterface,
) : ContainerHost<ConfirmationState, ConfirmationSideEffect>,
    ViewModel() {

    override val container =
        container<ConfirmationState, ConfirmationSideEffect>(ConfirmationState())

    private var cardId = ""
    private var orderId = ""

    fun setInitialState(
        cardId: String,
        orderId: String,
        phoneNumber: String,
        card: String,
        amount: String,
        commission: String
    ) {

        this.cardId = cardId
        this.orderId = orderId

        intent {
            reduce {
                state.copy(
                    phoneNumber = phoneNumber,
                    card = card,
                    amount = amount,
                    commission = commission
                )
            }
        }
    }

    fun requestPaymentOtp() = intent {
        loading(true)
        when (val response = api.paymentOtpRequest(scenario = PaymentOtpScenario.PAY_BY_CARD)) {
            is ApiResponse.Success -> {
                postSideEffect(ConfirmationSideEffect.ShowOtp(response.value.timeout, orderId))
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    fun pay() = intent {
        loading(true)
        val body = Pay(
            cardId = cardId,
            orderId = orderId,
        )
        when (val response = api.pay(body = body)) {
            is ApiResponse.Success -> {
                postSideEffect(ConfirmationSideEffect.OpenSuccessPayment(response.value.receipt))
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    private suspend fun SimpleSyntax<ConfirmationState, ConfirmationSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<ConfirmationState, ConfirmationSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            else -> {
                postSideEffect(ConfirmationSideEffect.ShowError(error.message, error.code))
            }
        }
    }
}
