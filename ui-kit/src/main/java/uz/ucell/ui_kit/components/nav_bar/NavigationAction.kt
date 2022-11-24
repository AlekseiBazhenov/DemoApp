package uz.ucell.ui_kit.components.nav_bar

sealed class NavigationAction {
    class Back(val onBackClick: (() -> Unit)) : NavigationAction()
    class Close(val onCloseClick: (() -> Unit)) : NavigationAction()
    object Empty : NavigationAction()
}
