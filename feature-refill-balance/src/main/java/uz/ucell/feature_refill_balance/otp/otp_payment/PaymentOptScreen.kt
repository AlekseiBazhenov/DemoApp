package uz.ucell.feature_refill_balance.otp.otp_payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.ucell.feature_refill_balance.otp.OtpScreen
import uz.ucell.networking.network.response.PayResponse

@Composable
fun PaymentOtpScreen(
    viewModel: PaymentOtpViewModel = viewModel(),
    modifier: Modifier = Modifier,
    timeout: Long,
    cardId: String,
    cardInfo: String,
    orderId: String,
    onPayed: (PayResponse.Receipt) -> Unit
) {
    LaunchedEffect(cardId) {
        viewModel.setInitialState(timeout, cardId, orderId)
    }

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is PaymentOtpSideEffect.ShowSuccessPaymentScreen -> onPayed(it.receipt)
                is PaymentOtpSideEffect.ShowError -> {}
                is PaymentOtpSideEffect.ShowOptError -> {}
            }
        }
    }

    val state = viewModel.container.stateFlow.collectAsState().value

    OtpScreen(
        modifier = modifier,
        state = state,
        maskedCardNumber = cardInfo,
        onCodeInputChanged = { viewModel.onSmsCodeInputChanged(it) },
        onRepeatOtpClick = { viewModel.onRepeatOtpClick() }
    )
}
