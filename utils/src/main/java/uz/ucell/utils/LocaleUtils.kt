package uz.ucell.utils

import android.content.Context
import java.util.Locale

object LocaleUtils {

    fun changeAppLanguage(context: Context, code: String) {
        val locale = Locale(code)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
