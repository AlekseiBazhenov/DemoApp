package uz.ucell.feature_initialization.contract

sealed class InitializationSideEffect {
    object OpenSelectLanguageScreen : InitializationSideEffect()
    object OpenPinAuthScreen : InitializationSideEffect()
    data class OpenEnterPhoneScreen(val selectedLanguage: String) : InitializationSideEffect()
}
