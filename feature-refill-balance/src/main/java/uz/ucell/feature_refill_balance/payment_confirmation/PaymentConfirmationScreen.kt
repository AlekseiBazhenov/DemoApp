package uz.ucell.feature_refill_balance.payment_confirmation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.ucell.feature_refill_balance.NEED_OTP
import uz.ucell.feature_refill_balance.NOT_NEED_OTP
import uz.ucell.feature_refill_balance.R
import uz.ucell.feature_refill_balance.RESERVED_FOR_UZCARD
import uz.ucell.feature_refill_balance.payment_confirmation.contract.ConfirmationSideEffect
import uz.ucell.networking.network.response.PayResponse
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonPrimary

@Composable
fun PaymentConfirmationScreen(
    viewModel: PaymentConfirmationViewModel = viewModel(),
    modifier: Modifier = Modifier,
    phoneNumber: String,
    cardId: String,
    card: String,
    amount: String,
    orderId: String,
    commission: String,
    otpStatus: Int,
    onNeedShowOtp: (Long, String) -> Unit,
    onPayed: (PayResponse.Receipt) -> Unit
) {
    LaunchedEffect(card) {
        viewModel.setInitialState(cardId, orderId, phoneNumber, card, amount, commission)
    }

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is ConfirmationSideEffect.ShowError -> {}
                is ConfirmationSideEffect.ShowOptError -> {}
                is ConfirmationSideEffect.OpenSuccessPayment -> onPayed(it.receipt)
                is ConfirmationSideEffect.ShowOtp -> onNeedShowOtp(it.timeout, it.orderId)
            }
        }
    }

    val state = viewModel.container.stateFlow.collectAsState().value

    Column(modifier = modifier) {
        Texts.H3(
            modifier = Modifier.padding(bottom = 28.dp),
            title = stringResource(R.string.payment_confirmation_title)
        )

        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 4.dp),
            title = stringResource(R.string.payment_confirmation_recipient),
            color = colorResource(R.color.sky3)
        )
        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 16.dp),
            title = state.phoneNumber,
        )

        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 4.dp),
            title = stringResource(R.string.payment_confirmation_from),
            color = colorResource(R.color.sky3)
        )
        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 16.dp),
            title = state.card,
        )

        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 4.dp),
            title = stringResource(R.string.amount),
            color = colorResource(R.color.sky3)
        )
        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 16.dp),
            title = state.amount,
        )

        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 4.dp),
            title = stringResource(R.string.commission),
            color = colorResource(R.color.sky3)
        )
        Texts.ParagraphText(
            modifier = Modifier.padding(bottom = 40.dp),
            title = state.commission,
        )
        ButtonPrimary(text = stringResource(R.string.button_confirm)) {
            when (otpStatus) {
                NOT_NEED_OTP -> viewModel.pay()
                NEED_OTP -> viewModel.requestPaymentOtp()
                RESERVED_FOR_UZCARD -> {
                    // Сейчас не приходит. Зарезервирован для доработок от Uzcard
                }
            }
        }
    }
}
