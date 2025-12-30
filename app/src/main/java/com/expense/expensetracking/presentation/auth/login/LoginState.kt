package com.expense.expensetracking.presentation.auth.login

import com.expense.expensetracking.common.util.UiState

data class LoginState(
    val uiState: UiState = UiState.Idle,
    val email: String = "",
    val password: String = "",
    val isChecked: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isRememberMe: Boolean = false,
)