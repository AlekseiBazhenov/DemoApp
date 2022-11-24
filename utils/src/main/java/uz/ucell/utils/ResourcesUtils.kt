package uz.ucell.utils

import android.content.Context
import androidx.annotation.StringRes
import java.util.Locale

object ResourcesUtils {

    fun getStringForLanguage(context: Context, language: String, @StringRes stringId: Int) =
        getResourcesForLanguage(context, language).getString(stringId)

    private fun getResourcesForLanguage(context: Context, language: String): Context {
        val locale = Locale(language)
        val config = context.resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
