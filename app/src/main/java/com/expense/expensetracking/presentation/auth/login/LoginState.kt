package com.expense.expensetracking.presentation.auth.login

import com.expense.expensetracking.common.util.UiState

data class LoginState(
    val uiState: UiState = UiState.Idle,
    val email: String = "",
    val password: String = "",
    val isChecked: Boolean = false,
    val isPasswordVisible: Boolean = false
)