package uz.ucell.app

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.HiltAndroidApp
import uz.ucell.utils.PhoneUtils
import javax.inject.Inject

@HiltAndroidApp
class UcellApp : Application() {

    @Inject
    lateinit var phoneUtils: PhoneUtils

    override fun onCreate() {
        super.onCreate()

        val config: YandexMetricaConfig = YandexMetricaConfig.newConfigBuilder(
            BuildConfig.APP_METRICA_API_KEY
        ).build()

        YandexMetrica.activate(this, config)
        YandexMetrica.enableActivityAutoTracking(this)

        YandexMetrica.setUserProfileID(phoneUtils.getDeviceId())
    }
}
