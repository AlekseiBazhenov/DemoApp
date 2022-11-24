package uz.ucell.feature_otp.sms

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import uz.ucell.core.constatns.CAPTCHA_LENGTH
import uz.ucell.core.constatns.RU
import uz.ucell.core.constatns.SMS_CODE_LENGTH
import uz.ucell.feature_otp.R
import uz.ucell.feature_otp.sms.contract.EnterSmsCodeState
import uz.ucell.feature_otp.sms.mock.MockEnterSmsCodeStateProvider
import uz.ucell.feature_otp.sms.views.Captcha
import uz.ucell.ui_kit.components.ActionLabel
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.inputs.CodeInput
import uz.ucell.ui_kit.components.nav_bar.NavigationAction
import uz.ucell.ui_kit.components.nav_bar.UcellAppBar
import uz.ucell.utils.formatSeconds

@Composable
fun SmsCodeScreen(
    state: EnterSmsCodeState,
    onCodeValueChanged: (String) -> Unit,
    onCaptchaValueChanged: (String) -> Unit,
    onRepeatSmsClicked: () -> Unit,
    onRefreshCaptchaClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.systemBarsPadding()) {
        UcellAppBar(
            navigationAction = NavigationAction.Back(onBackClick = { onBackClick() }),
            elevation = animateDpAsState(if (scrollState.value == 0) 0.dp else 8.dp).value
        )
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 32.dp)
        ) {
            if (state.needShowCaptcha.not()) {
                Texts.H2(
                    modifier = Modifier.padding(top = 10.dp),
                    title = stringResource(R.string.auth_sms_code_title)
                )
                Texts.ParagraphText(
                    modifier = Modifier.padding(top = 4.dp),
                    title = stringResource(R.string.auth_sms_code_subtitle, state.phoneNumber)
                )
            }
            CodeInput(
                modifier = Modifier.padding(top = 20.dp),
                input = state.codeInput,
                inputEnabled = state.isCodeInputEnabled && state.needShowCaptcha.not(),
                codeLength = SMS_CODE_LENGTH,
                isError = state.hasCodeInputError && state.needShowCaptcha.not(),
                hint = state.codeInputError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                onValueChanged = onCodeValueChanged
            )
            ActionLabel(
                modifier = Modifier.padding(vertical = 24.dp),
                text = getRepeatSmsLabelText(state.isTimerFinished, state.needShowCaptcha, state.timeoutSeconds),
                isEnabled = state.isTimerFinished && state.needShowCaptcha.not()
            ) {
                onRepeatSmsClicked()
            }
        }
        if (state.needShowCaptcha) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(colorResource(R.color.sky0))
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 24.dp)
            ) {
                Texts.H4(title = stringResource(R.string.auth_sms_code_captcha_title))
                CodeInput(
                    modifier = Modifier.padding(top = 20.dp),
                    input = state.captchaInput,
                    inputEnabled = true,
                    codeLength = CAPTCHA_LENGTH,
                    isError = state.hasCaptchaInputError,
                    hint = state.captchaInputError,
                    onValueChanged = onCaptchaValueChanged
                )
                Captcha(
                    image = state.captcha,
                    isLoading = state.captchaLoading,
                    onRefreshCaptchaClick = {
                        onRefreshCaptchaClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun getRepeatSmsLabelText(timerFinished: Boolean, needShowCaptcha: Boolean, timeoutSeconds: Long) =
    if (timerFinished || needShowCaptcha) {
        stringResource(R.string.otp_repeat)
    } else {
        stringResource(
            R.string.otp_repeat_with_timer,
            formatSeconds(timeoutSeconds)
        )
    }

@Preview(
    name = "sms code preview",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = RU
)
@Composable
fun SmsCodeScreenPreview(
    @PreviewParameter(MockEnterSmsCodeStateProvider::class) mockSmsCodeState: EnterSmsCodeState
) {
    SmsCodeScreen(
        state = mockSmsCodeState,
        onCodeValueChanged = {},
        onCaptchaValueChanged = {},
        onRepeatSmsClicked = {},
        onRefreshCaptchaClick = {},
        onBackClick = {}
    )
}
