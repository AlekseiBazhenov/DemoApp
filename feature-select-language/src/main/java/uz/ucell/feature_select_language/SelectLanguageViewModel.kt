package uz.ucell.feature_select_language

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.ucell.appmetrica.api.useCase.ChoiceLangAnalyticsUseCase
import uz.ucell.appmetrica.model.ContentStatus
import uz.ucell.core.constatns.DEFAULT_LANGUAGE
import uz.ucell.core.constatns.allLanguages
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.feature_select_language.contract.SelectLanguageSideEffect
import uz.ucell.feature_select_language.contract.SelectLanguageState
import uz.ucell.utils.PhoneUtils
import javax.inject.Inject

@HiltViewModel
class SelectLanguageViewModel @Inject constructor(
    private val appConfigPrefs: CoreStorage,
    private val phoneUtils: PhoneUtils,
    private val choiceLangAnalyticsUseCase: ChoiceLangAnalyticsUseCase
) : ContainerHost<SelectLanguageState, SelectLanguageSideEffect>, ViewModel() {
    override val container =
        container<SelectLanguageState, SelectLanguageSideEffect>(SelectLanguageState())

    init {
        getLanguages()
    }

    private fun getLanguages() = intent {
        val systemLanguage = phoneUtils.getDeviceLanguage()
        val languages = allLanguages.filter { it.value.enabled }

        val primaryLanguage =
            languages.getOrDefault(systemLanguage, languages[DEFAULT_LANGUAGE])!!.code
        val secondaryLanguage = languages.keys.first { it != primaryLanguage }

        reduce {
            state.copy(
                languages = languages.values.toList(),
                primaryLanguage = primaryLanguage,
                secondaryLanguage = secondaryLanguage
            )
        }
    }

    fun onScreenEvent() = intent {
        choiceLangAnalyticsUseCase.sendScreenInitEvent(ContentStatus.Available, state.primaryLanguage)
    }

    fun saveSelectedLanguage(newLanguage: String, languageStringValue: String) {
        intent {
            appConfigPrefs.saveSelectedLanguage(newLanguage)
            choiceLangAnalyticsUseCase.clickAuthChoiceLang(languageStringValue)
            postSideEffect(SelectLanguageSideEffect.OpenAuthScreen)
        }
    }
}
