package uz.ucell.feature_refill_balance.selected_card

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.ucell.feature_refill_balance.bank_card.PaymentSystemsAvailable
import uz.ucell.feature_refill_balance.selected_card.contract.SelectedCardSideEffect
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

@Composable
fun SelectedBankCardView(
    viewModel: SelectedCardViewModel = viewModel(),
    modifier: Modifier = Modifier,
    selectedCard: CardData,
    amount: String,
    msisdn: String,
    continueButtonEnabled: Boolean,
    onSelectedCardClick: () -> Unit,
    onNeedShowConfirmation: (String, Double) -> Unit
) {

    LaunchedEffect(selectedCard) {
        viewModel.setInitialState(amount, msisdn)
    }

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is SelectedCardSideEffect.OpenPaymentConfirmation -> onNeedShowConfirmation(
                    it.orderId,
                    it.commission,
                )
                is SelectedCardSideEffect.ShowError -> {
                }
            }
        }
    }

    val state = viewModel.container.stateFlow.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            ),
    ) {
        Texts.H4(title = stringResource(R.string.refill_balance_source_card))

        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .defaultMinSize(minHeight = 184.dp)
        ) {
            Row(
                modifier = Modifier.clickableWithoutIndication { onSelectedCardClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinkedCardInfo(selectedCard)
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.ic_arrow_right_18),
                    contentDescription = ""
                )
            }
        }

        ButtonPrimary(
            text = stringResource(R.string.button_continue),
            enabled = continueButtonEnabled,
            isLoading = state.isLoading
        ) {
            viewModel.onContinueButtonClick()
        }
    }
}

data class CardData(
    val id: String,
    val available: Boolean,
    val cardTitle: String,
    val cardInfo: String,
    val otpStatus: Int,
    val paymentSystem: PaymentSystemsAvailable?,
    @DrawableRes val image: Int
)
