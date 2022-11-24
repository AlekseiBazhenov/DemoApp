package uz.ucell.feature_refill_balance.bank_card

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.core.constatns.CARD_NUMBER_LAST_DIGITS
import uz.ucell.feature_refill_balance.NEED_OTP
import uz.ucell.feature_refill_balance.NOT_NEED_OTP
import uz.ucell.feature_refill_balance.RESERVED_FOR_UZCARD
import uz.ucell.feature_refill_balance.bank_card.contract.BankCardInputSideEffect
import uz.ucell.feature_refill_balance.bank_card.contract.BankCardInputState
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.network.PaymentOtpScenario
import uz.ucell.networking.network.request_body.AddNewCard
import uz.ucell.networking.network.request_body.CheckNewCard
import javax.inject.Inject

@HiltViewModel
class LinkBankCardViewModel @Inject constructor(
    private val api: NetworkingInterface
) : ContainerHost<BankCardInputState, BankCardInputSideEffect>, ViewModel() {

    override val container =
        container<BankCardInputState, BankCardInputSideEffect>(BankCardInputState())

    fun onBankCardNewValue(enteredNumber: String) = intent {
        reduce {
            state.copy(card = enteredNumber)
        }

        getCardType(enteredNumber)
    }

    private fun getCardType(cardNumber: String) = intent {
        val paymentSystem = BankCardUtils.getCardPaymentSystem(cardNumber)
        reduce {
            state.copy(cardPaymentSystem = paymentSystem)
        }
    }

    fun onBankCardMonthNewValue(enteredMonth: String) = intent {
        reduce {
            state.copy(month = enteredMonth)
        }

        if (enteredMonth.length == MONTH_YEAR_MAX_LENGTH) {
            postSideEffect(BankCardInputSideEffect.ReplaceFocusToYear)
        }
    }

    fun onBankCardYearNewValue(enteredYear: String) = intent {
        reduce {
            state.copy(year = enteredYear)
        }
    }

    fun onAddButtonClicked() = intent {
        loading(true)
        val requestBody = CheckNewCard(state.card, state.getExpire())
        when (val response = api.checkNewCard(requestBody)) {
            is ApiResponse.Success -> {
                when (response.value.otpStatus) {
                    NOT_NEED_OTP -> addNewCard()
                    NEED_OTP -> requestOtp()
                    RESERVED_FOR_UZCARD -> {
                        // Сейчас не приходит. Зарезервирован для доработок от Uzcard
                        postSideEffect(
                            BankCardInputSideEffect.ShowOpt(
                                timeout = 0,
                                card = state.card,
                                expires = state.getExpire()
                            )
                        )
                    }
                }
            }
            is ApiResponse.Error -> response.error?.let { processError(it) }
        }
        loading(false)
    }

    private fun requestOtp() = intent {
        loading(true)
        when (val response = api.paymentOtpRequest(scenario = PaymentOtpScenario.ADD_NEW_CARD)) {
            is ApiResponse.Success -> {
                postSideEffect(
                    BankCardInputSideEffect.ShowOpt(
                        timeout = response.value.timeout,
                        card = state.card,
                        expires = state.getExpire()
                    )
                )
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    private fun addNewCard() = intent {
        loading(true)
        val requestBody = AddNewCard(state.card, state.getExpire())
        when (val response = api.addNewCard(requestBody)) {
            is ApiResponse.Success -> {
                postSideEffect(
                    BankCardInputSideEffect.CardBound(
                        state.card.takeLast(CARD_NUMBER_LAST_DIGITS)
                    )
                )
            }
            is ApiResponse.Error -> {
                response.error?.let { processError(it) }
            }
        }
        loading(false)
    }

    private suspend fun SimpleSyntax<BankCardInputState, BankCardInputSideEffect>.loading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<BankCardInputState, BankCardInputSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            else -> postSideEffect(BankCardInputSideEffect.ShowError(error.message, error.code))
        }
    }
}
