package uz.ucell.feature_refill_balance.bank_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.ucell.feature_refill_balance.bank_card.contract.BankCardInputSideEffect
import uz.ucell.feature_refill_balance.bank_card.contract.BankCardInputState
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.buttons.ButtonPrimary
import uz.ucell.ui_kit.components.inputs.TextFieldStyle
import uz.ucell.ui_kit.components.inputs.TextInputs
import uz.ucell.utils.BankCardFormatter

@Composable
fun LinkBankCardView(
    viewModel: LinkBankCardViewModel = viewModel(),
    buttonTitle: String,
    modifier: Modifier = Modifier,
    onNewCardAdded: (String) -> Unit,
    onNeedShowOtp: (Long, String, String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is BankCardInputSideEffect.ReplaceFocusToYear -> focusRequester.requestFocus()
                is BankCardInputSideEffect.ShowError -> {
                }
                is BankCardInputSideEffect.ShowOpt -> onNeedShowOtp(it.timeout, it.card, it.expires)
                is BankCardInputSideEffect.CardBound -> onNewCardAdded(it.cardNumberLastDigits)
            }
        }
    }

    val state = viewModel.container.stateFlow.collectAsState().value

    Column(modifier = modifier) {
        CardFields(
            state = state,
            focusRequester = focusRequester,
            onCardValueChanged = { viewModel.onBankCardNewValue(it) },
            onMonthValueChanged = { viewModel.onBankCardMonthNewValue(it) },
            onYearValueChanged = { viewModel.onBankCardYearNewValue(it) }
        )
        ButtonPrimary(text = buttonTitle, isLoading = state.isLoading) {
            viewModel.onAddButtonClicked()
        }
    }
}

@Composable
private fun CardFields(
    state: BankCardInputState,
    focusRequester: FocusRequester,
    onCardValueChanged: (String) -> Unit,
    onMonthValueChanged: (String) -> Unit,
    onYearValueChanged: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .background(
                color = colorResource(R.color.sky0),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SupportedCardIconsRow(modifier = Modifier.padding(bottom = 8.dp))
            TextInputs.SingleLineTextInput(
                modifier = Modifier.padding(bottom = 12.dp),
                title = stringResource(R.string.bank_card_number),
                style = TextFieldStyle.WHITE,
                value = state.card,
                imagePainter = state.cardPaymentSystem?.let { getCardIcon(it) },
                onValueChange = { newText ->
                    val newTextFiltered = newText.filter { it.isDigit() }.take(BANK_CARD_MAX_LENGTH)
                    onCardValueChanged(newTextFiltered)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                isError = false,
                visualTransformation = remember { BankCardTransformation() },
            )
            CardMonthYearRow(
                focusRequester = focusRequester,
                month = state.month,
                year = state.year,
                onMonthValueChanged = {
                    onMonthValueChanged(it)
                },
                onYearValueChanged = {
                    onYearValueChanged(it)
                }
            )
        }
    }
}

@Composable
private fun getCardIcon(it: PaymentSystemsAll) = when (it) {
    PaymentSystemsAll.HUMO -> painterResource(R.drawable.ic_logo_color_humo_24)
    PaymentSystemsAll.UZCARD -> painterResource(R.drawable.ic_logo_color_uzcard_24)
}

@Composable
private fun SupportedCardIconsRow(modifier: Modifier = Modifier) {
    Row(modifier) {
        val iconColor = colorResource(R.color.sky3)
        Icon(
            painter = painterResource(R.drawable.ic_logo_grey_humo_32),
            tint = iconColor,
            contentDescription = null
        )
        Icon(
            painter = painterResource(R.drawable.ic_logo_grey_uzcard_32),
            tint = iconColor,
            contentDescription = null
        )
    }
}

@Composable
private fun CardMonthYearRow(
    focusRequester: FocusRequester,
    month: String,
    year: String,
    modifier: Modifier = Modifier,
    onMonthValueChanged: (String) -> Unit,
    onYearValueChanged: (String) -> Unit,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        TextInputs.SingleLineTextInput(
            modifier = Modifier.width(100.dp),
            title = stringResource(R.string.bank_card_month),
            style = TextFieldStyle.WHITE,
            value = month,
            onValueChange = { newText ->
                val newTextFiltered = newText.filter { it.isDigit() }.take(MONTH_YEAR_MAX_LENGTH)
                onMonthValueChanged(newTextFiltered)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester.requestFocus() }
            ),
            isError = false,
        )
        Texts.H3(modifier = Modifier.padding(horizontal = 8.dp), title = "/")
        TextInputs.SingleLineTextInput(
            modifier = Modifier.width(100.dp),
            focusRequester = focusRequester,
            title = stringResource(R.string.bank_card_year),
            style = TextFieldStyle.WHITE,
            value = year,
            onValueChange = { newText ->
                val newTextFiltered = newText.filter { it.isDigit() }.take(MONTH_YEAR_MAX_LENGTH)
                onYearValueChanged(newTextFiltered)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            isError = false,
        )
    }
}

const val BANK_CARD_MAX_LENGTH = 16
const val MONTH_YEAR_MAX_LENGTH = 2

/**
 * Visual transformation for mask `#### #### #### ####`
 */
private class BankCardTransformation : VisualTransformation {
    private val offsetTranslator = OffsetTranslator()

    override fun filter(text: AnnotatedString): TransformedText {
        val outText = BankCardFormatter.formatCard(text.text)
        return TransformedText(AnnotatedString(outText), offsetTranslator)
    }

    private inner class OffsetTranslator : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            when (offset) {
                in 0..4 -> offset
                in 5..8 -> offset + 1
                in 9..12 -> offset + 2
                in 13..16 -> offset + 3
                else -> 19
            }

        override fun transformedToOriginal(offset: Int): Int =
            when (offset) {
                in 0..4 -> offset
                in 5..8 -> offset - 1
                in 9..12 -> offset - 2
                in 13..16 -> offset - 3
                else -> 16
            }
    }
}
