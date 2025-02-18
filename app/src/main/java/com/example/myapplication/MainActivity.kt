package com.example.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.RoomDatabase
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lux: Sensor? = null
    private var stringLux: String = "huh?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSensors()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "DefaultScreen") {
                    composable("DefaultScreen") {
                        Default(navController, stringLux)
                    }
                    composable("MessageScreen") {
                        MessageScreen(navController)
                    }
                }
            }
        }
    }

    private fun lux(lux: Float): String {
        return when (lux.toInt()) {
            0 -> "DARK"
            in 1..100 -> "still pretty dark here.."
            in 101..5000 -> "It's getting brighter"
            in 5001..25000 -> "Turn the lights off.."
            else -> "Blind"
        }
    }

    private fun setSensors() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lux = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light = event.values[0]
            stringLux = lux(light)
            showNotification(this, "Your light-level!", "${lux(light)}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, lux, SensorManager.SENSOR_DELAY_NORMAL)
    }
}


