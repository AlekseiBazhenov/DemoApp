package uz.ucell.feature_select_language.contract

sealed class SelectLanguageSideEffect {
    data class ShowError(val text: String, val code: String) : SelectLanguageSideEffect()
    object OpenAuthScreen : SelectLanguageSideEffect()
}
