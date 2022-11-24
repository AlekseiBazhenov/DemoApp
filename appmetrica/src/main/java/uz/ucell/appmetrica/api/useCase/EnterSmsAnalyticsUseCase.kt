package uz.ucell.appmetrica.api.useCase

import kotlinx.coroutines.flow.first
import uz.ucell.appmetrica.api.UCellAnalytics
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core_storage.api.CoreStorage
import javax.inject.Inject

/**
 * Events
 */
private const val SCREEN_VIEW_AUTH_OTP: String = "screenview_auth_otp"
private const val CLICK_AUTH_OTP_REPEAT: String = "click_auth_otp_repeat"
private const val CLICK_AUTH_OTP_INPUT_SMS: String = "click_auth_otp_inputSms"
private const val CLICK_AUTH_OTP_INPUT_CAPTCHA: String = "click_auth_otp_inputCaptcha"
private const val CLICK_AUTH_OTP_UPDATE_CAPTCHA: String = "click_auth_otp_updateCaptcha"
private const val CLICK_AUTH_OTP_EXPIRED: String = "click_auth_otpExpired"
private const val CLICK_AUTH_OTP_LIMIT: String = "click_auth_otpLimit"

/**
 * Const
 */
private const val COMPONENT: String = "auth"
private const val CORRECT_CODE: String = "Правильный код"
private const val INCORRECT_CODE: String = "Неправильный код"
private const val DIALOG_RESULT_PRIMARY: String = "Получить SMS"
private const val DIALOG_RESULT_SECONDARY: String = "Отмена"

interface EnterSmsAnalyticsUseCase {
    suspend fun sendScreenMetric(contentStatus: ContentStatus)
    suspend fun sendClickAuthOtpRepeat()
    suspend fun sendClickAuthInputSms(isCodeCorrect: Boolean)
    suspend fun sendClickAuthInputCaptcha(isCodeCorrect: Boolean)
    suspend fun sendClickAuthUpdateCaptcha()
    suspend fun sendClickAuthOtpExpired(isPrimaryResult: Boolean)
    suspend fun sendClickAuthOtpLimit(isPrimaryResult: Boolean)
}

class EnterSmsAnalyticsUseCaseImpl @Inject constructor(
    private val analytics: UCellAnalytics,
    private val coreStorage: CoreStorage,
) : EnterSmsAnalyticsUseCase {
    override suspend fun sendScreenMetric(contentStatus: ContentStatus) {
        analytics.sendScreenMetric(
            screenLabel = SCREEN_VIEW_AUTH_OTP,
            contentStatus = contentStatus,
            lang = coreStorage.getSelectedLanguage().first(),
            component = COMPONENT
        )
    }

    override suspend fun sendClickAuthOtpRepeat() {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_OTP_REPEAT,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendClickAuthInputSms(isCodeCorrect: Boolean) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_OTP_INPUT_SMS,
            lang = coreStorage.getSelectedLanguage().first(),
            value = if (isCodeCorrect) CORRECT_CODE else INCORRECT_CODE
        )
    }

    override suspend fun sendClickAuthInputCaptcha(isCodeCorrect: Boolean) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_OTP_INPUT_CAPTCHA,
            lang = coreStorage.getSelectedLanguage().first(),
            value = if (isCodeCorrect) CORRECT_CODE else INCORRECT_CODE
        )
    }

    override suspend fun sendClickAuthUpdateCaptcha() {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_OTP_UPDATE_CAPTCHA,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendClickAuthOtpExpired(isPrimaryResult: Boolean) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_OTP_EXPIRED,
            lang = coreStorage.getSelectedLanguage().first(),
            value = if (isPrimaryResult) DIALOG_RESULT_PRIMARY else DIALOG_RESULT_SECONDARY
        )
    }

    override suspend fun sendClickAuthOtpLimit(isPrimaryResult: Boolean) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_OTP_LIMIT,
            lang = coreStorage.getSelectedLanguage().first(),
            value = if (isPrimaryResult) DIALOG_RESULT_PRIMARY else DIALOG_RESULT_SECONDARY
        )
    }
}
