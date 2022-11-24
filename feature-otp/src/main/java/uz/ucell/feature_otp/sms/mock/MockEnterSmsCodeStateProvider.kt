package uz.ucell.feature_otp.sms.mock

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import uz.ucell.feature_otp.sms.contract.EnterSmsCodeState

class MockEnterSmsCodeStateProvider : PreviewParameterProvider<EnterSmsCodeState> {
    override val values: Sequence<EnterSmsCodeState> = sequenceOf(
        EnterSmsCodeState(needShowCaptcha = true),
        EnterSmsCodeState(codeInput = "123"),
    )
}
