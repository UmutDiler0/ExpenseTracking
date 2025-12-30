package com.expense.expensetracking.presentation.settings

import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.data.manager.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : BaseViewModel<SettingsState, SettingsIntent>(
    initialState = SettingsState()
) {

    init {
        loadDarkModePreference()
    }

    private fun loadDarkModePreference() {
        viewModelScope.launch {
            val isDark = dataStoreManager.isDarkMode.firstOrNull() ?: false
            handleDataState {
                copy(isDarkMode = isDark)
            }
        }
    }

    override public fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ToggleDarkMode -> {
                toggleDarkMode(intent.isDark)
            }
            is SettingsIntent.ChangeLanguage -> {
                handleDataState {
                    copy(selectedLanguage = intent.language)
                }
            }
            is SettingsIntent.ChangePassword -> {
                // UI only - navigation will be handled in the screen
            }
            is SettingsIntent.DeleteAccount -> {
                // UI only - navigation will be handled in the screen
            }
        }
    }

    private fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveDarkMode(isDark)
            handleDataState {
                copy(isDarkMode = isDark)
            }
        }
    }
}
