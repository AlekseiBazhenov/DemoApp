package uz.ucell.appmetrica.api.useCase

import kotlinx.coroutines.flow.first
import uz.ucell.appmetrica.api.UCellAnalytics
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core_storage.api.CoreStorage
import javax.inject.Inject

private const val COMPONENT: String = "auth"
private const val SCREEN_VIEW_AUTH_CHOICE_LANG: String = "screenview_${COMPONENT}_choiceLang"
private const val CLICK_AUTH_CHOICE_LANG: String = "click_${COMPONENT}_choiceLang"
private const val CLICK_AUTH_HELP: String = "click_${COMPONENT}_help"

interface ChoiceLangAnalyticsUseCase {
    suspend fun sendScreenInitEvent(contentStatus: ContentStatus, lang: String)
    suspend fun clickAuthChoiceLang(value: String)
    suspend fun clickAuthHelp()
}

class ChoiceLangAnalyticsUseCaseImpl @Inject constructor(
    private val analytics: UCellAnalytics,
    private val coreStorage: CoreStorage
) : ChoiceLangAnalyticsUseCase {
    override suspend fun sendScreenInitEvent(contentStatus: ContentStatus, lang: String) {
        analytics.sendScreenMetric(
            screenLabel = SCREEN_VIEW_AUTH_CHOICE_LANG,
            contentStatus = contentStatus,
            lang = lang,
            component = COMPONENT,
        )
    }

    override suspend fun clickAuthChoiceLang(value: String) {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_CHOICE_LANG,
            lang = coreStorage.getSelectedLanguage().first(),
            value = value
        )
    }

    override suspend fun clickAuthHelp() {
        analytics.sendClickMetric(
            elementLabel = CLICK_AUTH_HELP,
            lang = coreStorage.getSelectedLanguage().first(),
        )
    }
}
