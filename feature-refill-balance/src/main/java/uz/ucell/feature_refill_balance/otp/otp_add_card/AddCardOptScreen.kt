package uz.ucell.feature_refill_balance.otp.otp_add_card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.ucell.core.constatns.CARD_NUMBER_LAST_DIGITS
import uz.ucell.feature_refill_balance.otp.OtpScreen

@Composable
fun AddCardOtpScreen(
    viewModel: AddCardOtpViewModel = viewModel(),
    modifier: Modifier = Modifier,
    timeout: Long,
    card: String,
    expires: String,
    onCardBound: (String) -> Unit
) {
    LaunchedEffect(card) {
        viewModel.setInitialState(timeout, card, expires)
    }

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is OtpSideEffect.CardBound -> onCardBound(card.takeLast(CARD_NUMBER_LAST_DIGITS))
                is OtpSideEffect.ShowError -> {}
                is OtpSideEffect.ShowOptError -> {}
            }
        }
    }

    val state = viewModel.container.stateFlow.collectAsState().value

    OtpScreen(
        modifier = modifier,
        state = state,
        maskedCardNumber = card.takeLast(CARD_NUMBER_LAST_DIGITS),
        onCodeInputChanged = { viewModel.onSmsCodeInputChanged(it) },
        onRepeatOtpClick = { viewModel.onRepeatOtpClick() }
    )
}
