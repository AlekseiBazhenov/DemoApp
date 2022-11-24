package uz.ucell.appmetrica.api.useCase

import kotlinx.coroutines.flow.first
import uz.ucell.appmetrica.api.UCellAnalytics
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core_storage.api.CoreStorage
import javax.inject.Inject

/**
 * Events
 */
private const val CLICK_LOGIN_BIOMETRY: String = "click_login_biometry"
private const val SCREEN_PARAMS_LOGIN_BIOMETRY: String = "screenparams_login_biometry"
private const val CONVERSION_LOGIN: String = "conversion_login"
private const val CLICK_LOGIN_PIN: String = "click_login_pin"
private const val CLICK_LOGIN_LOGOUT: String = "click_login_logout"
private const val SCREEN_VIEW_LOGIN_PIN: String = "screenview_login_pin"

/**
 * Const
 */
private const val COMPONENT: String = "login"
private const val BIOMETRIC: String = "Биометрия"
private const val SUCCESS_LOGIN: String = "Успешный вход (Биометрия)"
private const val FAILURE_LOGIN: String = "Неуспешный вход (Биометрия)"
private const val CORRECT_CODE: String = "Правильный код"
private const val INCORRECT_CODE: String = "Неправильный код"
private const val PIN: String = "Неправильный код"

interface AuthAnalyticsUseCase {
    suspend fun sendScreenMetric(contentStatus: ContentStatus)
    suspend fun sendClickLoginBiometry(isSuccess: Boolean)
    suspend fun sendClickLoginPin(isCodeCorrect: Boolean)
    suspend fun sendClickLogout()
    suspend fun sendConversionPinLogin()
    suspend fun sendConversionBiometryLogin()
    suspend fun sendModalLoginBiometry()
}

class AuthAnalyticsUseCaseImpl @Inject constructor(
    private val coreStorage: CoreStorage,
    private val analytics: UCellAnalytics
) : AuthAnalyticsUseCase {
    override suspend fun sendScreenMetric(contentStatus: ContentStatus) {
        analytics.sendScreenMetric(
            screenLabel = SCREEN_VIEW_LOGIN_PIN,
            contentStatus = contentStatus,
            lang = coreStorage.getSelectedLanguage().first(),
            component = COMPONENT
        )
    }

    override suspend fun sendClickLoginBiometry(isSuccess: Boolean) {
        analytics.sendClickMetric(
            elementLabel = CLICK_LOGIN_BIOMETRY,
            lang = coreStorage.getSelectedLanguage().first(),
            value = if (isSuccess) SUCCESS_LOGIN else FAILURE_LOGIN
        )
    }

    override suspend fun sendClickLoginPin(isCodeCorrect: Boolean) {
        analytics.sendClickMetric(
            elementLabel = CLICK_LOGIN_PIN,
            lang = coreStorage.getSelectedLanguage().first(),
            value = if (isCodeCorrect) CORRECT_CODE else INCORRECT_CODE
        )
    }

    override suspend fun sendClickLogout() {
        analytics.sendClickMetric(
            elementLabel = CLICK_LOGIN_LOGOUT,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendConversionPinLogin() = sendConversionLogin(PIN)

    override suspend fun sendConversionBiometryLogin() = sendConversionLogin(BIOMETRIC)

    private suspend fun sendConversionLogin(value: String) {
        analytics.sendConversionMetric(
            actionLabel = CONVERSION_LOGIN,
            lang = coreStorage.getSelectedLanguage().first(),
            value = value
        )
    }

    override suspend fun sendModalLoginBiometry() {
        analytics.sendModalInitMetric(
            modalLabel = SCREEN_PARAMS_LOGIN_BIOMETRY,
            lang = coreStorage.getSelectedLanguage().first(),
            value = BIOMETRIC
        )
    }
}
