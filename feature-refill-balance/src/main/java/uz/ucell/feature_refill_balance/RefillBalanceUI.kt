package uz.ucell.feature_refill_balance

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.ucell.core.keyboardVisibleAsState
import uz.ucell.feature_refill_balance.bank_card.LinkBankCardView
import uz.ucell.feature_refill_balance.contract.RefillBalanceState
import uz.ucell.feature_refill_balance.otp.otp_add_card.AddCardOtpScreen
import uz.ucell.feature_refill_balance.otp.otp_payment.PaymentOtpScreen
import uz.ucell.feature_refill_balance.payment_confirmation.PaymentConfirmationScreen
import uz.ucell.feature_refill_balance.selected_card.CardData
import uz.ucell.feature_refill_balance.selected_card.SelectedBankCardView
import uz.ucell.feature_refill_balance.views.CardBoundScreen
import uz.ucell.feature_refill_balance.views.LinkedCardsList
import uz.ucell.feature_refill_balance.views.RefillBalanceInfo
import uz.ucell.networking.network.response.PayResponse
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.UcellModalBottomSheet
import uz.ucell.ui_kit.components.divider.BlockDivider
import uz.ucell.ui_kit.components.nav_bar.NavigationAction
import uz.ucell.ui_kit.components.nav_bar.ScrollStrategy
import uz.ucell.ui_kit.components.nav_bar.collapsing.CollapsingToolbarScaffold
import uz.ucell.ui_kit.components.nav_bar.collapsing.rememberCollapsingToolbarScaffoldState
import uz.ucell.utils.PhoneFormatter

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun RefillBalanceScreen(
    state: RefillBalanceState,
    onBackClick: () -> Unit,
    onPhoneValueChanged: (String) -> Unit,
    onSumValueChanged: (String) -> Unit,
    onRecommendedSumClicked: (String) -> Unit,
    onBankCardSelected: (CardData) -> Unit,
    onSuccessPayment: (PayResponse.Receipt) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    var currentBottomSheet: BottomSheetScreen? by remember { mutableStateOf(null) }

    val closeSheet: () -> Unit = {
        currentBottomSheet = null
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }

    val openSheet: (BottomSheetScreen) -> Unit = {
        currentBottomSheet = it
        coroutineScope.launch {
            modalBottomSheetState.show()
        }
    }

    BackHandler(modalBottomSheetState.isVisible) {
        coroutineScope.launch { modalBottomSheetState.hide() }
    }

    UcellModalBottomSheet(
        modalBottomSheetState,
        content = {
            val toolbarScaffoldState = rememberCollapsingToolbarScaffoldState()
            val scrollState = rememberScrollState()

            val keyboardVisible by keyboardVisibleAsState()
            coroutineScope.launch {
                if (keyboardVisible) {
                    toolbarScaffoldState.toolbarState.collapse()
                } else {
                    toolbarScaffoldState.toolbarState.expand()
                }
            }

            CollapsingToolbarScaffold(
                modifier = Modifier.systemBarsPadding(),
                scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                state = toolbarScaffoldState,
                enabled = false,
                navigationAction = NavigationAction.Back(onBackClick = { onBackClick() }),
                title = stringResource(R.string.refill_balance_title)
            ) {
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(
                            state = scrollState,
                            reverseScrolling = true
                        )
                    ) {
                        RefillBalanceInfo(
                            state,
                            onPhoneValueChanged,
                            onSumValueChanged,
                            onRecommendedSumClicked
                        )
                        BlockDivider()

                        if (state.isCardsLoading.not()) {
                            if (state.linkedCards.isEmpty()) {
                                LinkBankCardView(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 24.dp
                                    ),
                                    buttonTitle = stringResource(R.string.button_continue),
                                    onNeedShowOtp = { timeout, card, expires ->
                                        openSheet(
                                            BottomSheetScreen.AddCardOtp(
                                                timeout,
                                                card,
                                                expires
                                            )
                                        )
                                    },
                                    onNewCardAdded = {
                                        // todo fetch card list
                                        openSheet(BottomSheetScreen.CardBound(it))
                                    }
                                )
                            } else {
                                state.selectedCard?.let {
                                    SelectedBankCardView(
                                        selectedCard = it,
                                        amount = state.amountInput,
                                        msisdn = state.phoneInput,
                                        continueButtonEnabled = true, // todo validation result
                                        onSelectedCardClick = {
                                            openSheet(BottomSheetScreen.SelectCard(state.linkedCards))
                                        },
                                        onNeedShowConfirmation = { orderId, commission ->
                                            openSheet(
                                                BottomSheetScreen.PaymentConfirmation(
                                                    orderId = orderId,
                                                    commission = commission
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        sheetContent = {
            currentBottomSheet?.let { sheetScreen ->
                when (sheetScreen) {
                    is BottomSheetScreen.SelectCard -> {
                        LinkedCardsList(
                            modifier = Modifier.padding(vertical = 24.dp),
                            cards = sheetScreen.cards,
                            selectedCardId = state.selectedCard?.id,
                            onCardItemClicked = {
                                onBankCardSelected(it)
                                closeSheet()
                            },
                            onAddNewCardClicked = { openSheet(BottomSheetScreen.AddNewCard) }
                        )
                    }
                    is BottomSheetScreen.AddNewCard -> {
                        LinkBankCardView(
                            modifier = Modifier.padding(vertical = 24.dp),
                            buttonTitle = stringResource(R.string.button_add_card),
                            onNeedShowOtp = { timeout, card, expires ->
                                openSheet(BottomSheetScreen.AddCardOtp(timeout, card, expires))
                            },
                            onNewCardAdded = {
                                // todo fetch card list
                                openSheet(BottomSheetScreen.CardBound(it))
                            }
                        )
                    }
                    is BottomSheetScreen.AddCardOtp -> {
                        AddCardOtpScreen(
                            modifier = Modifier
                                .fillMaxHeight(0.8f)
                                .padding(vertical = 24.dp),
                            timeout = sheetScreen.timeout,
                            card = sheetScreen.cardInput,
                            expires = sheetScreen.expires,
                            onCardBound = {
                                // todo fetch card list
                                openSheet(BottomSheetScreen.CardBound(it))
                            }
                        )
                    }
                    is BottomSheetScreen.PaymentOtp -> {
                        PaymentOtpScreen(
                            modifier = Modifier
                                .fillMaxHeight(0.8f)
                                .padding(vertical = 24.dp),
                            timeout = sheetScreen.timeout,
                            cardId = state.selectedCard!!.id,
                            cardInfo = state.selectedCard.cardInfo,
                            orderId = sheetScreen.orderId,
                            onPayed = {
                                openSheet(BottomSheetScreen.PaymentSuccess(it))
                            }
                        )
                    }
                    is BottomSheetScreen.PaymentConfirmation -> {
                        PaymentConfirmationScreen(
                            modifier = Modifier.padding(vertical = 24.dp),
                            phoneNumber = PhoneFormatter.formatPhone(state.phoneInput),
                            cardId = state.selectedCard!!.id,
                            card = state.selectedCard.cardInfo,
                            amount = state.amountInput,
                            orderId = sheetScreen.orderId,
                            commission = sheetScreen.commission.toString(),
                            otpStatus = state.selectedCard.otpStatus,
                            onNeedShowOtp = { timeout, orderId ->
                                openSheet(
                                    BottomSheetScreen.PaymentOtp(
                                        timeout = timeout,
                                        cardId = state.selectedCard.id,
                                        orderId = orderId
                                    )
                                )
                            },
                            onPayed = {
                                onSuccessPayment(it)
                            }
                        )
                    }
                    is BottomSheetScreen.CardBound -> {
                        CardBoundScreen(
                            modifier = Modifier
                                .fillMaxHeight(0.8f)
                                .padding(vertical = 24.dp),
                            cardNumberLastDigits = sheetScreen.cardNumberLastDigits,
                            onContinueClicked = { closeSheet() }
                        )
                    }
                    is BottomSheetScreen.PaymentSuccess -> {
                        closeSheet()
                        onSuccessPayment(sheetScreen.receipt)
                    }
                }
            }
        }
    )
}

sealed class BottomSheetScreen {

    object AddNewCard : BottomSheetScreen()

    data class SelectCard(val cards: List<CardData>) : BottomSheetScreen()

    data class AddCardOtp(
        val timeout: Long,
        val cardInput: String,
        val expires: String
    ) : BottomSheetScreen()

    data class PaymentOtp(
        val timeout: Long,
        val cardId: String,
        val orderId: String
    ) : BottomSheetScreen()

    data class CardBound(val cardNumberLastDigits: String) : BottomSheetScreen()

    data class PaymentConfirmation(
        val orderId: String,
        val commission: Double
    ) : BottomSheetScreen()

    data class PaymentSuccess(val receipt: PayResponse.Receipt) : BottomSheetScreen()
}
