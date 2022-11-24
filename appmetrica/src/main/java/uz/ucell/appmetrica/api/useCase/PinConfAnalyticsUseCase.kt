package uz.ucell.appmetrica.api.useCase

import kotlinx.coroutines.flow.first
import uz.ucell.appmetrica.api.UCellAnalytics
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core_storage.api.CoreStorage
import javax.inject.Inject

/**
 * Const
 */
private const val COMPONENT: String = "auth"
private const val PIN: String = "PIN"
private const val CORRECT_CODE: String = "Правильный код"
private const val INCORRECT_CODE: String = "Неправильный код"
private const val ALERT_RESULT_PRIMARY: String = "Да (Биометрия)"
private const val ALERT_RESULT_SECONDARY: String = "Нет (Биометрия)"

/**
 * Events
 */
private const val SCREEN_VIEW_AUTH_PIN_REPEAT: String = "screenview_auth_pinRepeat"
private const val CLICK_AUTH_PIN_REPEAT: String = "click_auth_pinRepeat"
private const val SCREEN_PARAMS_AUTH_BIOMETRY: String = "screenparams_auth_biometry"
private const val CLICK_AUTH_BIOMETRY: String = "click_auth_biometry"
private const val CONVERSION_AUTH: String = "click_auth_pinRepeat"

interface PinConfAnalyticsUseCase {
    suspend fun sendScreenMetric(contentStatus: ContentStatus)
    suspend fun sendClickPinRepeat(isCodeCorrect: Boolean)
    suspend fun sendConversionAuth()
    suspend fun sendClickAuthBiometry(isSuccess: Boolean)
    suspend fun sendModalAuthBiometry()
}

class PinConfAnalyticsUseCaseImpl @Inject constructor(
    private val coreStorage: CoreStorage,
    private val analytics: UCellAnalytics
) : PinConfAnalyticsUseCase {
    override suspend fun sendScreenMetric(contentStatus: ContentStatus) {
        analytics.sendScreenMetric(
            screenLabel = SCREEN_VIEW_AUTH_PIN_REPEAT,
            contentStatus = contentStatus,
            lang = coreStorage.getSelectedLanguage().first(),
            component = COMPONENT
        )
    }

    override suspend fun sendClickPinRepeat(isCodeCorrect: Boolean) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_PIN_REPEAT,
            lang = coreStorage.getSelectedLanguage().first(),
            value = if (isCodeCorrect) CORRECT_CODE else INCORRECT_CODE
        )
    }

    override suspend fun sendConversionAuth() {
        analytics.sendConversionMetric(
            actionLabel = CONVERSION_AUTH,
            lang = coreStorage.getSelectedLanguage().first(),
            value = PIN
        )
    }

    override suspend fun sendClickAuthBiometry(isSuccess: Boolean) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_BIOMETRY,
            lang = coreStorage.getSelectedLanguage().first(),
            value = if (isSuccess) ALERT_RESULT_PRIMARY else ALERT_RESULT_SECONDARY
        )
    }

    override suspend fun sendModalAuthBiometry() {
        analytics.sendModalInitMetric(
            modalLabel = SCREEN_PARAMS_AUTH_BIOMETRY,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }
}
