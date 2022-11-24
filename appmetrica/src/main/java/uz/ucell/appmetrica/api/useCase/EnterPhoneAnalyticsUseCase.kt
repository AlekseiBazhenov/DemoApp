package uz.ucell.appmetrica.api.useCase

import kotlinx.coroutines.flow.first
import uz.ucell.appmetrica.api.UCellAnalytics
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core_storage.api.CoreStorage
import javax.inject.Inject

private const val COMPONENT: String = "auth"
private const val SCREEN_PARAMS_AUTH_CHANGE_LANG: String = "screenparams_${COMPONENT}_changeLang"
private const val SCREEN_VIEW_AUTH_PHONE_NUMBER: String = "screenview_${COMPONENT}_phoneNumber"
private const val CLICK_AUTH_PHONE_NUMBER_LANG: String = "click_${COMPONENT}_phoneNumber_lang"
private const val CLICK_AUTH_PHONE_NUMBER_NEXT: String = "click_${COMPONENT}_phoneNumber_next"
private const val CLICK_AUTH_PHONE_NUMBER_OFERTA: String = "click_${COMPONENT}_phoneNumber_oferta"
private const val CLICK_AUTH_HELP: String = "click_${COMPONENT}_help"
private const val CLICK_AUTH_CHANGE_LANG: String = "click_${COMPONENT}_changeLang"

interface EnterPhoneAnalyticsUseCase {
    suspend fun sendScreenMetric(contentStatus: ContentStatus)
    suspend fun sendScreenParamsChangeLang()
    suspend fun sendClickAuthNumberLang()
    suspend fun sendClickAuthNumberNext()
    suspend fun sendClickAuthOffer()
    suspend fun sendClickAuthHelp()
    suspend fun sendClickAuthChangeLang(languageCode: String)
}

class EnterPhoneAnalyticsUseCaseImpl @Inject constructor(
    private val analytics: UCellAnalytics,
    private val coreStorage: CoreStorage
) : EnterPhoneAnalyticsUseCase {
    override suspend fun sendScreenMetric(contentStatus: ContentStatus) {
        analytics.sendScreenMetric(
            screenLabel = SCREEN_VIEW_AUTH_PHONE_NUMBER,
            contentStatus = contentStatus,
            lang = coreStorage.getSelectedLanguage().first(),
            component = COMPONENT
        )
    }

    override suspend fun sendScreenParamsChangeLang() {
        analytics.sendModalInitMetric(
            modalLabel = SCREEN_PARAMS_AUTH_CHANGE_LANG,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendClickAuthNumberLang() {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_PHONE_NUMBER_LANG,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendClickAuthNumberNext() {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_PHONE_NUMBER_NEXT,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendClickAuthOffer() {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_PHONE_NUMBER_OFERTA,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendClickAuthHelp() {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_HELP,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }

    override suspend fun sendClickAuthChangeLang(languageCode: String) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_CHANGE_LANG,
            lang = languageCode,
        )
    }
}
