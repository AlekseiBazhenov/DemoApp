package uz.ucell.debugpanel

import kotlinx.coroutines.flow.Flow

interface ShakeListener {
    fun registerListener()
    fun unregisterListener()
    fun shakeListener(): Flow<Unit>
}
