package uz.ucell.feature_refill_balance.selected_card

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.feature_refill_balance.selected_card.contract.SelectedCardSideEffect
import uz.ucell.feature_refill_balance.selected_card.contract.SelectedCardState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.network.request_body.PayOrder
import javax.inject.Inject

@HiltViewModel
class SelectedCardViewModel @Inject constructor(
    private val api: NetworkingInterface,
) : ContainerHost<SelectedCardState, SelectedCardSideEffect>,
    ViewModel() {

    override val container =
        container<SelectedCardState, SelectedCardSideEffect>(SelectedCardState())

    var amount: String = ""
    var msisdn: String = ""

    fun setInitialState(amount: String, msisdn: String) {
        this.amount = amount
        this.msisdn = msisdn
    }

    fun onContinueButtonClick() = intent {
        loading(true)
        val body = PayOrder(
            amount = amount.toInt(),
            msisdn = msisdn,
        )
        when (val response = api.payOrder(body = body)) {
            is ApiResponse.Success -> {
                postSideEffect(
                    SelectedCardSideEffect.OpenPaymentConfirmation(
                        orderId = response.value.orderId,
                        commission = response.value.commission
                    )
                )
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    private suspend fun SimpleSyntax<SelectedCardState, SelectedCardSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<SelectedCardState, SelectedCardSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            else -> {
                postSideEffect(SelectedCardSideEffect.ShowError(error.message, error.code))
            }
        }
    }
}
