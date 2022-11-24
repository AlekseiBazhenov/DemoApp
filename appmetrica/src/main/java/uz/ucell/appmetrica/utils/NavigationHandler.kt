package uz.ucell.appmetrica.utils

import javax.inject.Inject

interface NavigationHandler {
    fun onScreenEvent(newScreen: String, component: String)
    fun getCurrentScreen(): String
    fun getComponent(): String
    fun getPreviousScreen(): String?
}

class NavigationHandlerImpl @Inject constructor() : NavigationHandler {
    private var currentScreen: String? = null
    private var previousScreen: String? = null
    private var component: String? = null

    override fun getCurrentScreen(): String = currentScreen ?: DEFAULT_SCREEN

    override fun getComponent(): String = component ?: DEFAULT_COMPONENT

    override fun onScreenEvent(newScreen: String, component: String) {
        if (currentScreen == newScreen) return

        this.previousScreen = currentScreen
        this.currentScreen = newScreen
        this.component = component
    }

    override fun getPreviousScreen(): String? = previousScreen
}

/**
 * The first screen that the user can see was selected as the default one
 */
private const val DEFAULT_SCREEN: String = "screenview_auth_choiceLang"
private const val DEFAULT_COMPONENT: String = "auth"
