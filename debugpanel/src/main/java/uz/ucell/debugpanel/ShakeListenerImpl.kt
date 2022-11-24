package uz.ucell.debugpanel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import java.util.Objects
import kotlin.math.sqrt

class ShakeListenerImpl(private val context: Context) : ShakeListener {

    private val channel = Channel<Unit>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private var accel = 0f
    private var accelCurrent = 0f
    private var accelLast = 0f
    private val sensorListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            accelLast = accelCurrent
            accelCurrent = sqrt(x * x + y * y)
            val delta = accelCurrent - accelLast
            accel = accel * 0.9f + delta
            if (accel > 12) {
                runBlocking {
                    channel.send(Unit)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    init {
        Objects.requireNonNull(sensorManager)
            .registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
            )
        accel = 10f
        accelCurrent = SensorManager.GRAVITY_EARTH
        accelLast = SensorManager.GRAVITY_EARTH
    }

    override fun registerListener() {
        sensorManager.registerListener(
            sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun unregisterListener() {
        sensorManager.unregisterListener(sensorListener)
    }

    override fun shakeListener(): Flow<Unit> = channel.receiveAsFlow()
}
