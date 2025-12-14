package com.expense.expensetracking.presentation.auth.register

sealed class RegisterIntent {
    data class SetEmail(val email: String): RegisterIntent()
    data class SetPassword(val password: String): RegisterIntent()
    data object SetPasswordVisiblity: RegisterIntent()
}