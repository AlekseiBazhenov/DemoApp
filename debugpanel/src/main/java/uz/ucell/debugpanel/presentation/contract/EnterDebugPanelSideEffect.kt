package uz.ucell.debugpanel.presentation.contract

sealed class EnterDebugPanelSideEffect {
    data class ShowError(val text: String, val code: String) : EnterDebugPanelSideEffect()
}
