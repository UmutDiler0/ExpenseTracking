package com.expense.expensetracking.presentation.auth.fogot_password

import com.expense.expensetracking.common.util.UiState

data class FPState(
    val uiState: UiState = UiState.Idle,
    val email: String = ""
)