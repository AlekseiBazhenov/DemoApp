package uz.ucell.appmetrica.api.useCase

import kotlinx.coroutines.flow.first
import uz.ucell.appmetrica.api.UCellAnalytics
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core_storage.api.CoreStorage
import javax.inject.Inject

/**
 * Events
 */
private const val SCREEN_VIEW_AUTH_PIN_SETUP: String = "screenview_auth_pinSetup"
private const val CLICK_AUTH_PIN_SETUP: String = "click_auth_pinSetup"
private const val CLICK_AUTH_PIN_SETUP_SIMPLE_CODE: String = "click_auth_pinSetup_simpleCode"

/**
 * Const
 */
private const val COMPONENT: String = "auth"
private const val SETUP_CONTINUE: String = "Продолжить с этим"
private const val SETUP_ENTER_NEW: String = "Ввести другой код"

interface PinCreationAnalyticsUseCase {
    suspend fun sendScreenMetric(contentStatus: ContentStatus)
    suspend fun sendClickPinSetup()
    suspend fun sendClickPinSetupSimpleCodeContinue()
    suspend fun sendClickPinSetupSimpleCodeEnterNew()
}

class PinCreationAnalyticsUseCaseImpl @Inject constructor(
    private val coreStorage: CoreStorage,
    private val analytics: UCellAnalytics
) : PinCreationAnalyticsUseCase {
    override suspend fun sendScreenMetric(contentStatus: ContentStatus) {
        analytics.sendScreenMetric(
            screenLabel = SCREEN_VIEW_AUTH_PIN_SETUP,
            contentStatus = contentStatus,
            lang = coreStorage.getSelectedLanguage().first(),
            component = COMPONENT
        )
    }

    override suspend fun sendClickPinSetup() {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_PIN_SETUP,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendClickPinSetupSimpleCodeContinue() {
        sendClickPinSetupSimpleCode(SETUP_CONTINUE)
    }

    override suspend fun sendClickPinSetupSimpleCodeEnterNew() {
        sendClickPinSetupSimpleCode(SETUP_ENTER_NEW)
    }

    private suspend fun sendClickPinSetupSimpleCode(value: String) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_PIN_SETUP_SIMPLE_CODE,
            lang = coreStorage.getSelectedLanguage().first(),
            value = value
        )
    }
}
