package com.expense.expensetracking.presentation.profile.ui

sealed class ProfileIntent {
    data class SetBalanceLimit(val limit: Double) : ProfileIntent()
    data class ToggleNotification(val enabled: Boolean) : ProfileIntent()
    data class ShowLimitDialog(val show: Boolean) : ProfileIntent()
    data class UpdateTempLimit(val value: String) : ProfileIntent()
}