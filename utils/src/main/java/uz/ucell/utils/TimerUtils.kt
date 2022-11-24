package uz.ucell.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
fun timerFlow(
    initCountDownSeconds: Long,
    intervalMilliseconds: Long = TIMER_INTERVAL_MILLIS
) = flow {
    var countDownSeconds = initCountDownSeconds

    do {
        emit(countDownSeconds)
        --countDownSeconds
        delay(intervalMilliseconds)
    } while (countDownSeconds >= 0L)
}

private const val TIMER_INTERVAL_MILLIS = 1000L
