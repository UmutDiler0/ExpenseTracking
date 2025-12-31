package com.expense.expensetracking.presentation.settings

sealed class CurrencyLoadState {
    object Idle : CurrencyLoadState()
    object Loading : CurrencyLoadState()
    object Success : CurrencyLoadState()
    data class Error(val message: String) : CurrencyLoadState()
}

data class SettingsState(
    val isDarkMode: Boolean = false,
    val selectedLanguage: String = "Türkçe",
    val selectedCurrency: String = "TR",
    val currencyList: List<String> = emptyList(),
    val currencyLoadState: CurrencyLoadState = CurrencyLoadState.Loading
)
