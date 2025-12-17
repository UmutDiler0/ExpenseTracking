package com.expense.expensetracking.presentation.auth.register

import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.AuthErrors
import com.expense.expensetracking.common.util.Register
import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.repo.AuthRepository
import com.expense.expensetracking.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): BaseViewModel<RegisterState, RegisterIntent>(
    initialState = RegisterState(),
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
            is RegisterIntent.OnClickRegisterBtn -> {
                register()
            }
        }
    }
    fun register() {
        viewModelScope.launch {
            val currentState = uiDataState.value
            val newUser = User(
                email = currentState.email,
                password = currentState.password,
                name = currentState.name,
                surname = currentState.surname
            )

            authRepository.register(newUser).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        handleDataState {
                            copy(uiState = UiState.Loading)
                        }
                    }
                    is Resource.Success -> {
                        handleDataState {
                            copy(
                                uiState = UiState.Success,
                                isError = false,
                                errorMessage = ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        handleDataState {
                            copy(
                                uiState = UiState.Error(resource.message),
                                isError = true,
                                errorMessage = resource.message
                            )
                        }
                    }
                    is Resource.Idle -> {
                        handleDataState {
                            copy(uiState = UiState.Idle)
                        }
                    }
                }
            }
        }
    }
}