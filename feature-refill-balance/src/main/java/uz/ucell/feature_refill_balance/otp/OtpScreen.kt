package uz.ucell.feature_refill_balance.otp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.ucell.core.constatns.SMS_CODE_LENGTH
import uz.ucell.feature_refill_balance.R
import uz.ucell.ui_kit.components.ActionLabel
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.inputs.CodeInput
import uz.ucell.utils.formatSeconds

@Composable
fun OtpScreen(
    modifier: Modifier,
    state: OtpState,
    maskedCardNumber: String,
    onCodeInputChanged: (String) -> Unit,
    onRepeatOtpClick: () -> Unit
) {
    Column(modifier = modifier) {
        Texts.H4(
            modifier = Modifier.padding(bottom = 4.dp),
            title = stringResource(R.string.payment_otp_title)
        )
        Texts.Caption(
            title = stringResource(
                R.string.payment_otp_title,
                maskedCardNumber
            )
        )

        CodeInput(
            modifier = Modifier.padding(top = 20.dp),
            input = state.codeInput,
            codeLength = SMS_CODE_LENGTH,
            isError = state.hasCodeInputError,
            hint = state.codeInputError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            onValueChanged = onCodeInputChanged
        )
        ActionLabel(
            modifier = Modifier.padding(top = 24.dp, bottom = 196.dp),
            text = getRepeatSmsLabelText(state.isTimerFinished, state.timeoutSeconds),
            isEnabled = state.isTimerFinished,
            onClick = onRepeatOtpClick
        )
    }
}

@Composable
private fun getRepeatSmsLabelText(timerFinished: Boolean, timeoutSeconds: Long) =
    if (timerFinished)
        stringResource(R.string.otp_repeat)
    else
        stringResource(
            R.string.otp_repeat_with_timer,
            formatSeconds(timeoutSeconds)
        )
