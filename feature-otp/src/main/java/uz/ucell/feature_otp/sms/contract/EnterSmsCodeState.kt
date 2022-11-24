package uz.ucell.feature_otp.sms.contract

import android.graphics.Bitmap

data class EnterSmsCodeState(
    val isLoading: Boolean = false,
    val phoneNumber: String = "",
    val codeInput: String = "",
    val hasCodeInputError: Boolean = false,
    val codeInputError: String = "",
    val isCodeInputEnabled: Boolean = true,
    val timeoutSeconds: Long = 0,
    val isTimerFinished: Boolean = true,
    val needShowCaptcha: Boolean = false,
    val captchaInput: String = "",
    val captchaLoading: Boolean = false,
    val captcha: Bitmap? = null,
    val hasCaptchaInputError: Boolean = false,
    val captchaInputError: String = "",
)
