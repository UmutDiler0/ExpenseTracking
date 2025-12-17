package com.expense.expensetracking.presentation.auth.login

import com.expense.expensetracking.common.util.Login

sealed class LoginIntent {

    data class SetEmail(val email: String): LoginIntent()
    data class SetPassword(val password: String): LoginIntent()
    data object SetRememberme: LoginIntent()
    data object SetPasswordVisibility: LoginIntent()

    object ClickLoginBtn: LoginIntent()
}