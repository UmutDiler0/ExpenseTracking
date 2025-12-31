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
            is SettingsIntent.ChangeCurrency -> {
                handleDataState {
                    copy(selectedCurrency = intent.currency)
                }
            }
            is SettingsIntent.LoadCurrencies -> {
                loadCurrencies()
            }
            is SettingsIntent.ChangePassword -> {
                // UI only - navigation will be handled in the screen
            }
            is SettingsIntent.DeleteAccount -> {
                // UI only - navigation will be handled in the screen
            }
        }
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            handleDataState {
                copy(currencyLoadState = CurrencyLoadState.Loading)
            }
            
            // Simulate loading - şimdilik boş liste
            kotlinx.coroutines.delay(1000)
            
            handleDataState {
                copy(
                    currencyList = emptyList(),
                    currencyLoadState = CurrencyLoadState.Success
                )
            }
            
            // Success'ten sonra Idle'a geç
            kotlinx.coroutines.delay(100)
            handleDataState {
                copy(currencyLoadState = CurrencyLoadState.Idle)
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
