package uz.ucell.feature_refill_balance.contract

import uz.ucell.feature_refill_balance.selected_card.CardData
import uz.ucell.networking.network.response.PaymentLimitsResponse
import uz.ucell.ui_kit.components.chips.ChipsData

data class RefillBalanceState(
    val isCardsLoading: Boolean = false,
    val isError: Boolean = false,
    val userBalance: String = "",
    val phoneInput: String = "",
    val isOtherMsisdn: Boolean = false,
    val amountInput: String = "",
    val paymentRecommendations: List<ChipsData> = emptyList(),
    val paymentLimits: PaymentLimitsResponse? = null,
    val linkedCards: List<CardData> = emptyList(),
    val selectedCard: CardData? = null
)
