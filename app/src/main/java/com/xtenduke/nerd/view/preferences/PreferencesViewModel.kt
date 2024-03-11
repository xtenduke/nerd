package com.xtenduke.nerd.view.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtenduke.nerd.data.persistence.Preferences
import com.xtenduke.nerd.data.persistence.PreferencesStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// redundant right?
sealed class PreferencesState {
    data object Loading: PreferencesState()
    data class Data(val preferences: Preferences): PreferencesState()

    data class Error(val message: String): PreferencesState()
}

class PreferencesViewModel: ViewModel() {
    private val _preferencesState = MutableStateFlow<PreferencesState>(PreferencesState.Loading)
    val preferencesState: StateFlow<PreferencesState> = _preferencesState

    fun getPreferencesData() {
        viewModelScope.launch {
            try {
                val preferences = PreferencesStore().getPreferences();
                _preferencesState.value = PreferencesState.Data(preferences)
            } catch (e: Exception) {
                _preferencesState.value = PreferencesState.Error(e.message!!)
            }
        }
    }

}