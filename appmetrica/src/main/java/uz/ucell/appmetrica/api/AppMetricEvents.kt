package uz.ucell.appmetrica.api

import uz.ucell.appmetrica.model.MetricEventData

/**
 * Send user events in AppMetrica
 */
interface AppMetricEvents {
    fun publishEvent(event: MetricEventData)
}
