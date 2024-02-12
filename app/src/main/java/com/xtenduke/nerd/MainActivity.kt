package com.xtenduke.nerd

import android.os.Bundle
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.xtenduke.nerd.ui.theme.NerdTheme
import com.xtenduke.nerd.view.openhardwaremonitor.OpenHardwareMonitorState
import com.xtenduke.nerd.view.openhardwaremonitor.OpenHardwareMonitorViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val openHardwareMonitorViewModel: OpenHardwareMonitorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NerdTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(openHardwareMonitorViewModel = openHardwareMonitorViewModel)
                }
            }
        }

        openHardwareMonitorViewModel.viewModelScope.launch {
            beginUpdating()
        }
    }

    private suspend fun beginUpdating() {
        openHardwareMonitorViewModel.getOpenHardwareMonitorData()
        delay(1000)
        beginUpdating()
    }
}

@Composable
fun Greeting(openHardwareMonitorViewModel: OpenHardwareMonitorViewModel) {
    val state by openHardwareMonitorViewModel.openHardwareMonitorState.collectAsState()
    NerdTheme {
        when (state) {
            is OpenHardwareMonitorState.Success -> {
                val data = (state as OpenHardwareMonitorState.Success).openHardwareMonitorData
                Column {
                    Text(
                        text = data.cpu?.name ?: "",
                    )
                    Text(text = "Load: ${data.cpu?.totalLoad?.curr ?: ""}")
                    Text(text = "Package : ${data.cpu?.packageTemp?.curr ?: ""}")
                    Text(text = "Power: ${data.cpu?.packagePower?.curr ?: ""}")
                    Text(text = "Frequency Highest: ${data.cpu?.maxCoreFreq ?: ""}")
                    Text(text = "Frequency Lowest: ${data.cpu?.minCoreFreq ?: ""}")
                    Text(text = "Frequency Average: ${data.cpu?.avgCoreFreq ?: ""}")
                    
                    Spacer(modifier = Modifier.height(80.dp))
                    data.gpus.values.forEach {
                        Text(text = it.name ?: "")
                        Text(text = "Core Clock: ${it.coreClock?.curr ?: ""}")
                        Text(text = "Mem Clock: ${it.memoryClock?.curr ?: ""}")
                        Text(text = "Power: ${it.power?.curr ?: ""}")
                        Text(text = "Temp: ${it.coreTemp?.curr ?: ""}")
                        Text(text = "Load: ${it.coreLoad?.curr ?: ""}")
                    }
                }
            }
            is OpenHardwareMonitorState.Loading -> {
                Text(
                    text = "Loading",
                    fontSize = 45.sp,
                    color = Color(0, 0, 139),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
            }
            is OpenHardwareMonitorState.Error -> {
                val error = (state as OpenHardwareMonitorState.Error).message
                Text(
                    text = error,
                    fontSize = 45.sp,
                    color = Color(0, 0, 139),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
            }

        }
    }
}