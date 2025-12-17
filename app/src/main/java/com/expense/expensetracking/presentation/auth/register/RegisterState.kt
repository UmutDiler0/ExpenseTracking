package com.expense.expensetracking.presentation.auth.register

import com.expense.expensetracking.common.util.UiState

data class RegisterState(
    val uiState: UiState = UiState.Idle,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val surname: String = "",
    val isError: Boolean = false,
    val errorMessage: String = "",
    val registerStep: RegisterStep = RegisterStep.EMAIL_PASSWORD,
    val isPasswordVisible: Boolean = false
)
