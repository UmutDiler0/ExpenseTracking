package com.expense.expensetracking.presentation.settings

data class SettingsState(
    val isDarkMode: Boolean = false,
    val selectedLanguage: String = "Türkçe"
)
