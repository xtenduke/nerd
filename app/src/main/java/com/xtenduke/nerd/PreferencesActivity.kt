package com.xtenduke.nerd

import android.os.Bundle
import android.widget.ScrollView
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.core.view.ScrollingView
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.xtenduke.nerd.ui.theme.NerdTheme
import com.xtenduke.nerd.view.openhardwaremonitor.OpenHardwareMonitorViewModel
import com.xtenduke.nerd.view.preferences.PreferencesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PreferencesActivity: ComponentActivity() {
    private val preferencesViewModel: PreferencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NerdTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Preferences(preferencesViewModel = preferencesViewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Preferences(preferencesViewModel: PreferencesViewModel) {
        val navController = rememberNavController()
        NerdTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary
                        ),
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                    )
                },
                content = { innerPadding ->
                    Content(preferencesViewModel = preferencesViewModel, innerPadding)
                }
            )
        }
    }

    @Composable
    fun Content(preferencesViewModel: PreferencesViewModel, paddingValues: PaddingValues) {
        val state by preferencesViewModel.preferencesState.collectAsState()
        Text(text = "Sup turds")
    }
}