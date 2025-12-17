package com.expense.expensetracking.presentation.auth.register

sealed class RegisterIntent {
    data class SetEmail(val email: String): RegisterIntent()
    data class SetPassword(val password: String): RegisterIntent()

    data class SetName(val name: String): RegisterIntent()
    data class SetSurname(val surname: String): RegisterIntent()
    data class SetRegisterStep(val registerStep: RegisterStep): RegisterIntent()
    data object SetPasswordVisiblity: RegisterIntent()
}