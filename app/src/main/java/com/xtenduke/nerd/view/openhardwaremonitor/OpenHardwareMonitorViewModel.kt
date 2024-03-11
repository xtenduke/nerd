package com.xtenduke.nerd.view.openhardwaremonitor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtenduke.nerd.data.api.OpenHardwareMonitorApi
import com.xtenduke.nerd.data.model.OhmModelParsed
import com.xtenduke.nerd.data.persistence.AppDatabase
import com.xtenduke.nerd.data.persistence.DatabaseProvider
import com.xtenduke.nerd.data.persistence.PreferencesStore
import com.xtenduke.nerd.data.util.OpenHardwareMonitorParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

sealed class OpenHardwareMonitorState {
    data object Loading: OpenHardwareMonitorState()
    data class Success(val openHardwareMonitorData: OhmModelParsed): OpenHardwareMonitorState()
    data class Error(val message: String): OpenHardwareMonitorState()
}

class OpenHardwareMonitorViewModel: ViewModel() {
    private val _openHardwareMonitorState = MutableStateFlow<OpenHardwareMonitorState>(OpenHardwareMonitorState.Loading)
    val openHardwareMonitorState: StateFlow<OpenHardwareMonitorState> = _openHardwareMonitorState
    private lateinit var api: OpenHardwareMonitorApi

    fun getOpenHardwareMonitorData() {
        viewModelScope.launch {
            try {
                val preferences = PreferencesStore().getPreferences()
                viewModelScope
                api = OpenHardwareMonitorApi(preferences.target.host + ":" + preferences.target.port)
                val response = api.openHardwareMonitorService.getData()
                val parsed = OpenHardwareMonitorParser().parse(response)
                _openHardwareMonitorState.value = OpenHardwareMonitorState.Success(parsed)
            } catch (e: Exception) {
                _openHardwareMonitorState.value = OpenHardwareMonitorState.Error(e.message!!)
            }
        }
    }

}