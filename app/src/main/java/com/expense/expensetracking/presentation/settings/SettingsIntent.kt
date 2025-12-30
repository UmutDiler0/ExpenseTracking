package com.expense.expensetracking.presentation.settings

sealed class SettingsIntent {
    data class ToggleDarkMode(val isDark: Boolean) : SettingsIntent()
    data class ChangeLanguage(val language: String) : SettingsIntent()
    object ChangePassword : SettingsIntent()
    object DeleteAccount : SettingsIntent()
}
