package uz.ucell.feature_refill_balance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.feature_refill_balance.contract.RefillBalanceSideEffect
import uz.ucell.feature_refill_balance.contract.RefillBalanceState
import uz.ucell.feature_refill_balance.mappers.LinkedCardsMapper
import uz.ucell.feature_refill_balance.mappers.PaymentRecommendationsMapper
import uz.ucell.feature_refill_balance.selected_card.CardData
import uz.ucell.networking.NetworkingInterface
import uz.ucell.networking.data.ApiResponse
import uz.ucell.networking.data.ErrorBody
import uz.ucell.networking.network.response.LinkedCardsResponse
import uz.ucell.utils.PHONE_NUMBER_LENGTH
import uz.ucell.utils.PhoneFormatter
import javax.inject.Inject

@HiltViewModel
class RefillBalanceViewModel @Inject constructor(
    private val api: NetworkingInterface,
    private val coreStorage: CoreStorage,
    private val mapper: PaymentRecommendationsMapper,
    private val cardsMapper: LinkedCardsMapper,
) : ContainerHost<RefillBalanceState, RefillBalanceSideEffect>, ViewModel() {

    override val container =
        container<RefillBalanceState, RefillBalanceSideEffect>(RefillBalanceState())

    private var userMsisdn: String = ""

    init {
        intent {
            val msisdn = coreStorage.getMsisdn().first()
            val savedPhoneNumber = PhoneFormatter.removeCountryCode(msisdn)
            userMsisdn = savedPhoneNumber
            reduce {
                state.copy(phoneInput = savedPhoneNumber)
            }
            getLinkedCards()
            getBalance()
            getPaymentRecommendations()
            getPaymentLimits()
        }
    }

    fun onNewPhoneValue(enteredPhone: String) = intent {
        reduce {
            state.copy(
                phoneInput = enteredPhone,
                isOtherMsisdn = enteredPhone != userMsisdn,
            )
        }

        if (enteredPhone.length == PHONE_NUMBER_LENGTH) {
            getPaymentRecommendations()
        }
    }

    fun onNewAmountValue(enteredAmount: String) = intent {
        reduce {
            state.copy(amountInput = enteredAmount)
        }
    }

    fun onBankCardSelected(card: CardData) = intent {
        reduce {
            state.copy(selectedCard = card)
        }
    }

    private fun getLinkedCards() = intent {
        linkedCardsLoading(true)
        when (val response = api.getLinkedCards()) {
            is ApiResponse.Success -> {
//               val cards = cardsMapper.cards(response.value.cards),
                val cards = cardsMapper.cards(linkedCardsMock.cards)
                reduce {
                    state.copy(
                        linkedCards = cards,
                        selectedCard = cards.firstOrNull()
                    )
                }
            }
            is ApiResponse.Error -> response.error?.let { processError(it) }
        }
        linkedCardsLoading(false)
    }

    private fun getBalance() = intent {
        when (val response = api.getBalance()) {
            is ApiResponse.Success -> {
                reduce {
                    state.copy(userBalance = response.value.balance.toString())
                }
            }
            is ApiResponse.Error -> response.error?.let { processError(it) }
        }
    }

    private fun getPaymentRecommendations() = intent {
        when (val response = api.getPaymentRecommendations(state.isOtherMsisdn)) {
            is ApiResponse.Success -> {
                val recommendations = mapper.recommendations(response.value)
                val main = recommendations[0]
                reduce {
                    state.copy(
                        paymentRecommendations = recommendations,
                        amountInput = main.text
                    )
                }
            }
            is ApiResponse.Error -> response.error?.let { processError(it) }
        }
    }

    private fun getPaymentLimits() = intent {
        when (val response = api.getPaymentLimits()) {
            is ApiResponse.Success -> {
                reduce {
                    state.copy(paymentLimits = response.value)
                }
            }
            is ApiResponse.Error -> response.error?.let { processError(it) }
        }
    }

    // TODO: удалить после реализации добавления новой карты
    private val linkedCardsMock = LinkedCardsResponse(
        arrayListOf(
            LinkedCardsResponse.Card(
                id = "345579",
                available = false,
                description = "карта заблокирована",
                balance = 950000.0,
                maskedPan = "860012******2052",
                otpStatus = 0,
                linkedDate = "2021-08-23T22:54:07+05:00"
            ),
            LinkedCardsResponse.Card(
                id = "345678",
                available = true,
                balance = 2421.32,
                maskedPan = "860012******2051",
                otpStatus = 1,
                linkedDate = "2021-07-23T22:54:07+05:00"
            ),
            LinkedCardsResponse.Card(
                id = "345679",
                available = true,
                balance = 2123445.32,
                maskedPan = "860012******2060",
                otpStatus = 1,
                linkedDate = "2021-07-24T22:54:07+05:00"
            ),
            LinkedCardsResponse.Card(
                id = "345680",
                available = true,
                balance = 40000.32,
                maskedPan = "986012******2061",
                otpStatus = 1,
                linkedDate = "2021-07-25T22:54:07+05:00"
            ),
            LinkedCardsResponse.Card(
                id = "345681",
                available = true,
                balance = 410000.32,
                maskedPan = "986012******2062",
                otpStatus = 1,
                linkedDate = "2021-07-26T22:54:07+05:00"
            )

        )
    )

    private suspend fun SimpleSyntax<RefillBalanceState, RefillBalanceSideEffect>.linkedCardsLoading(
        loading: Boolean
    ) {
        reduce {
            state.copy(isCardsLoading = loading)
        }
    }

    private suspend fun SimpleSyntax<RefillBalanceState, RefillBalanceSideEffect>.processError(
        error: ErrorBody
    ) {
        when (error.code) {
            else -> postSideEffect(RefillBalanceSideEffect.ShowError(error.message, error.code))
        }
    }
}
