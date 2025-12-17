package com.expense.expensetracking.presentation.auth.register

import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.AuthErrors
import com.expense.expensetracking.common.util.Register
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
            is RegisterIntent.SetRegisterStep -> {
                if (intent.registerStep == RegisterStep.NAME_SURNAME) {

                    val email = uiDataState.value.email
                    val password = uiDataState.value.password

                    // Kural 1: Boş alan kontrolü (İsteğe bağlı ama önerilir)
                    if (email.isBlank() || password.isBlank()) {
                        handleDataState {
                            copy(isError = true, errorMessage = AuthErrors.EMPTY_FIELDS)
                        }
                        return@handleIntent
                    }

                    if (!email.contains("@gmail.com")) {
                        handleDataState {
                            copy(isError = true, errorMessage = AuthErrors.INVALID_GMAIL)
                        }
                        return@handleIntent
                    }

                    if (!password.any { it.isUpperCase() }) {
                        handleDataState {
                            copy(isError = true, errorMessage = AuthErrors.NO_UPPERCASE)
                        }
                        return@handleIntent
                    }

                    if (!password.any { it.isDigit() }) {
                        handleDataState {
                            copy(isError = true, errorMessage = AuthErrors.NO_DIGIT)
                        }
                        return@handleIntent
                    }
                }

                handleDataState {
                    copy(
                        registerStep = intent.registerStep,
                        isError = false,
                        errorMessage = ""
                    )
                }
            }
            is RegisterIntent.SetName -> {
                handleDataState {
                    copy(
                        name = intent.name
                    )
                }
            }
            is RegisterIntent.SetSurname -> {
                handleDataState {
                    copy(
                        surname = intent.surname
                    )
                }
            }
        }
    }
}