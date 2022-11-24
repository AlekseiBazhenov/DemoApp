package uz.ucell.appmetrica.model

import uz.ucell.appmetrica.NONE

data class ScreenMetric(
    val screenLabel: String,
    val component: String,
    val previousScreen: String?
) {
    fun getSavePreviousScreen(): String = previousScreen ?: NONE
}
