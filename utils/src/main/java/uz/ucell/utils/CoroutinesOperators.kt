package uz.ucell.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object CoroutinesOperators {
    fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
        var lastEmissionTime = 0L
        collect { upstream ->
            val currentTime = System.currentTimeMillis()
            val mayEmit = currentTime - lastEmissionTime > windowDuration
            if (mayEmit) {
                lastEmissionTime = currentTime
                emit(upstream)
            }
        }
    }
}
