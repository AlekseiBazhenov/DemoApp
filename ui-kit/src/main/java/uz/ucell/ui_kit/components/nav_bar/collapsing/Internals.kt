package uz.ucell.ui_kit.components.nav_bar

import android.os.SystemClock
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.util.VelocityTracker

/**
 * HACK: Compose tracks velocity with a local coordinate system which leads to an undesired
 * scroll experience. To mitigate this issue, we use RelativeVelocityTracker which tracks velocity
 * with a global coordinate system. In NestedScrollConnection, onPreScroll() gives us a delta
 * based on a global coordinate, we can use this value to properly calculate the velocity.
 *
 * The fundamental goal of this class is to override the Compose-calculated scroll velocity to
 * our manually calculated one.
 *
 * @see <a href="https://github.com/onebone/compose-collapsing-toolbar/issues/7">this issue</a>
 */
internal class RelativeVelocityTracker(
    private val timeProvider: CurrentTimeProvider
) {
    private val tracker = VelocityTracker()
    private var lastY: Float? = null

    fun delta(delta: Float) {
        val new = (lastY ?: 0f) + delta

        tracker.addPosition(timeProvider.now(), Offset(0f, new))
        lastY = new
    }

    fun reset(): Float {
        lastY = null

        val velocity = tracker.calculateVelocity()
        tracker.resetTracking()

        return velocity.y
    }
}

/**
 * [androidx.compose.ui.input.nestedscroll.NestedScrollConnection.onPreFling] subtracts its scroll
 * value by the returned value to calculate a remaining velocity.
 *
 * This function provides a delta that will override the remaining value that is calculated by the
 * compose framework.
 *
 * @see <a href="https://github.com/onebone/compose-collapsing-toolbar/issues/7">this issue</a>
 */
internal fun RelativeVelocityTracker.deriveDelta(initial: Float) =
    initial - reset()

internal interface CurrentTimeProvider {
    fun now(): Long
}

internal class CurrentTimeProviderImpl : CurrentTimeProvider {
    override fun now(): Long =
        SystemClock.uptimeMillis()
}
