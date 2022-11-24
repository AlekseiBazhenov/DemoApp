package uz.ucell.appmetrica.model

import org.json.JSONObject
import uz.ucell.appmetrica.CODE_ERROR
import uz.ucell.appmetrica.COMPONENT
import uz.ucell.appmetrica.CONTENT_STATUS
import uz.ucell.appmetrica.CURRENT_SCREEN
import uz.ucell.appmetrica.EVENT_NAME
import uz.ucell.appmetrica.EVENT_PARAMS
import uz.ucell.appmetrica.LANG
import uz.ucell.appmetrica.NONE
import uz.ucell.appmetrica.PREVIOUS_SCREEN
import uz.ucell.appmetrica.RATE_ID
import uz.ucell.appmetrica.TYPE
import uz.ucell.appmetrica.VALUE

sealed class MetricEventData {
    /**
     * The name of the component for registering events in YM
     */
    abstract val eventName: String

    abstract fun toJson(): JSONObject

    fun toJsonString(): String = toJson().toString()

    data class ScreenView(
        val screenLabel: String,
        val component: String,
        val contentStatus: ContentStatus,
        val lang: String,
        val previousScreen: String = NONE,
        val rateId: Int? = null,
    ) : MetricEventData() {
        override val eventName: String
            get() = screenLabel

        override fun toJson(): JSONObject {
            val json = JSONObject()
            json.put(EVENT_NAME, screenLabel)

            val eventParamsJson = JSONObject()
            eventParamsJson.put(TYPE, EventType.SCREEN_VIEW.eventName)
            eventParamsJson.put(COMPONENT, component)
            eventParamsJson.put(CONTENT_STATUS, contentStatus.status)
            if (contentStatus is ContentStatus.Error) {
                eventParamsJson.put(CODE_ERROR, contentStatus.errorCode)
            }
            eventParamsJson.put(LANG, lang)
            eventParamsJson.put(PREVIOUS_SCREEN, previousScreen)
            eventParamsJson.putOpt(RATE_ID, rateId)

            json.put(EVENT_PARAMS, eventParamsJson)

            return json
        }
    }

    data class ScreenParams(
        val currentModalComponentLabel: String,
        val component: String,
        val contentStatus: ContentStatus,
        val lang: String,
        val parentScreen: String,
        val previousScreen: String,
        val value: String? = null,
        val rateId: Int? = null
    ) : MetricEventData() {
        override val eventName: String
            get() = currentModalComponentLabel

        override fun toJson(): JSONObject {
            val json = JSONObject()
            json.put(EVENT_NAME, currentModalComponentLabel)

            val eventParamsJson = JSONObject()
            eventParamsJson.put(TYPE, EventType.SCREEN_PARAMS.eventName)
            eventParamsJson.put(COMPONENT, component)
            eventParamsJson.put(CONTENT_STATUS, contentStatus.status)
            if (contentStatus is ContentStatus.Error) {
                eventParamsJson.put(CODE_ERROR, contentStatus.errorCode)
            }
            eventParamsJson.put(LANG, lang)
            eventParamsJson.put(CURRENT_SCREEN, parentScreen)
            eventParamsJson.put(PREVIOUS_SCREEN, previousScreen)
            eventParamsJson.putOpt(VALUE, value)
            eventParamsJson.putOpt(RATE_ID, rateId)

            json.put(EVENT_PARAMS, eventParamsJson)

            return json
        }
    }

    data class Click(
        val elementLabel: String,
        val currentScreen: String,
        val component: String,
        val lang: String,
        val previousScreen: String = NONE,
        val value: String? = null,
        val rateId: Int? = null
    ) : MetricEventData() {
        override val eventName: String
            get() = elementLabel

        override fun toJson(): JSONObject {
            val json = JSONObject()
            json.put(EVENT_NAME, elementLabel)

            val eventParamsJson = JSONObject()
            eventParamsJson.put(TYPE, EventType.CLICK.eventName)
            eventParamsJson.put(COMPONENT, component)
            eventParamsJson.put(LANG, lang)
            eventParamsJson.put(CURRENT_SCREEN, currentScreen)
            eventParamsJson.put(PREVIOUS_SCREEN, previousScreen)
            eventParamsJson.putOpt(VALUE, value)
            eventParamsJson.putOpt(RATE_ID, rateId)

            json.put(EVENT_PARAMS, eventParamsJson)

            return json
        }
    }

    data class Conversion(
        val actionLabel: String,
        val component: String,
        val lang: String,
        val currentScreen: String,
        val previousScreen: String = NONE,
        val value: String? = null
    ) : MetricEventData() {
        override val eventName: String
            get() = actionLabel

        override fun toJson(): JSONObject {
            val json = JSONObject()
            json.put(EVENT_NAME, actionLabel)

            val eventParamsJson = JSONObject()
            eventParamsJson.put(TYPE, EventType.CONVERSION.eventName)
            eventParamsJson.put(COMPONENT, component)
            eventParamsJson.put(LANG, lang)
            eventParamsJson.put(CURRENT_SCREEN, currentScreen)
            eventParamsJson.put(PREVIOUS_SCREEN, previousScreen)
            eventParamsJson.putOpt(VALUE, value)

            json.put(EVENT_PARAMS, eventParamsJson)

            return json
        }
    }
}
