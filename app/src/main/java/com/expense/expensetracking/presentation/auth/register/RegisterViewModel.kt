package com.expense.expensetracking.presentation.auth.register

import com.expense.expensetracking.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(): BaseViewModel<RegisterState, RegisterIntent>(
    initialState = RegisterState()
) {
    public override fun handleIntent(intent: RegisterIntent) {
        when(intent) {
            is RegisterIntent.SetEmail -> {
                handleDataState { copy(email = intent.email) }
            }
            is RegisterIntent.SetPassword -> {
                handleDataState { copy(password = intent.password) }
            }
            RegisterIntent.SetPasswordVisiblity -> {
                handleDataState { copy(isPasswordVisible = !isPasswordVisible) }
            }
        }
    }
}