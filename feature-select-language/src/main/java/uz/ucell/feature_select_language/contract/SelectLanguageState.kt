package uz.ucell.feature_select_language.contract

import uz.ucell.core.models.Language

data class SelectLanguageState(
    val languages: List<Language> = emptyList(),
    val primaryLanguage: String = "",
    val secondaryLanguage: String = "",
)
