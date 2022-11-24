package uz.ucell.appmetrica.api.impl

import com.yandex.metrica.ValidationException
import com.yandex.metrica.YandexMetrica
import org.json.JSONException
import uz.ucell.appmetrica.api.AppMetricEvents
import uz.ucell.appmetrica.model.MetricEventData

internal class AppMetricEventsImpl : AppMetricEvents {
    override fun publishEvent(event: MetricEventData) {
        try {
            YandexMetrica.reportEvent(event.eventName, event.toJsonString())
        } catch (e: ValidationException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
