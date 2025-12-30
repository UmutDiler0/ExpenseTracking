package com.expense.expensetracking.presentation.auth.register

import com.expense.expensetracking.common.util.UiState

data class RegisterState(
    val uiState: UiState = UiState.Idle,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val name: String = "",
    val nameError: String? = null,
    val surname: String = "",
    val surnameError: String? = null,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val registerStep: RegisterStep = RegisterStep.EMAIL_PASSWORD,
    val isPasswordVisible: Boolean = false
)
