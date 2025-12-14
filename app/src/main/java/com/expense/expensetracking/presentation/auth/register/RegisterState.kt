package com.expense.expensetracking.presentation.auth.register

import com.expense.expensetracking.common.util.UiState

data class RegisterState(
    val uiState: UiState = UiState.Idle,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
)
