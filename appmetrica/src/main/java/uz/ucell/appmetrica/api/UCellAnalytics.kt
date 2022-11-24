package uz.ucell.appmetrica.api

import uz.ucell.appmetrica.NONE
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.appmetrica.model.MetricEventData
import uz.ucell.appmetrica.utils.NavigationHandler
import javax.inject.Inject

class UCellAnalytics @Inject constructor(
    private val events: AppMetricEvents,
    private val navigationHandler: NavigationHandler
) {
    fun getScreenLabel(): String = navigationHandler.getCurrentScreen()

    fun getComponent(): String = navigationHandler.getComponent()

    fun getPreviousScreen(): String = navigationHandler.getPreviousScreen() ?: NONE

    fun sendScreenMetric(
        screenLabel: String,
        contentStatus: ContentStatus,
        lang: String,
        component: String
    ) {
        navigationHandler.onScreenEvent(screenLabel, component)

        val event = MetricEventData.ScreenView(
            getScreenLabel(),
            component,
            contentStatus,
            lang,
            getPreviousScreen()
        )
        events.publishEvent(event)
    }

    fun sendClickMetric(
        elementLabel: String,
        lang: String,
        value: String? = null
    ) {
        val event = MetricEventData.Click(
            elementLabel,
            getScreenLabel(),
            getComponent(),
            lang,
            getPreviousScreen(),
            value = value
        )
        events.publishEvent(event)
    }

    fun sendModalInitMetric(
        modalLabel: String,
        lang: String,
        contentStatus: ContentStatus = ContentStatus.Available,
        value: String? = null
    ) {
        val event = MetricEventData.ScreenParams(
            modalLabel,
            getComponent(),
            contentStatus,
            lang,
            getScreenLabel(),
            getPreviousScreen(),
            value = value
        )
        events.publishEvent(event)
    }

    fun sendConversionMetric(
        actionLabel: String,
        lang: String,
        value: String? = null
    ) {
        val event = MetricEventData.Conversion(
            actionLabel,
            getComponent(),
            lang,
            getScreenLabel(),
            getPreviousScreen(),
            value = value
        )
        events.publishEvent(event)
    }
}
