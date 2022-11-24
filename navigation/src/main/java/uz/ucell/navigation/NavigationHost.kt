package uz.ucell.navigation

interface NavigationHost {
    fun navigateTo(destination: NavigationDestination)
    fun navigateBack()
}
