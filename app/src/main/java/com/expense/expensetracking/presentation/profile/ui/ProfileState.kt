package com.expense.expensetracking.presentation.profile.ui

import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.User

data class ProfileState(
    val uiState: UiState = UiState.Loading,
    val user: User? = null,
    val balanceLimit: Double = 0.0,
    val notificationEnabled: Boolean = false,
    val showLimitDialog: Boolean = false,
    val tempLimit: String = ""
)