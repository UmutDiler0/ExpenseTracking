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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): BaseViewModel<RegisterState, RegisterIntent>(
    initialState = RegisterState(),
) {

    init {
        observeEmailValidation()
        observePasswordValidation()
        observeNameValidation()
        observeSurnameValidation()
    }

    private fun observeEmailValidation() {
        viewModelScope.launch {
            uiDataState.map { it.email }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1200)
                .collect { email ->
                    val error = when {
                        email.isEmpty() -> "E-posta alanı boş bırakılamaz"
                        !email.contains("@gmail.com") -> "Sadece @gmail.com uzantılı adresler kabul edilir"
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "geçersiz e posta formatı"
                        else -> null
                    }
                    handleDataState { copy(emailError = error) }
                }
        }
    }

    private fun observePasswordValidation() {
        viewModelScope.launch {
            uiDataState.map { it.password }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1200)
                .collect { password ->
                    val hasUpperCase = password.any { it.isUpperCase() }
                    val hasDigit = password.any { it.isDigit() }
                    val isLongEnough = password.length >= 6

                    val error = when {
                        password.isEmpty() -> "Şifre alanı boş bırakılamaz"
                        !isLongEnough -> "Şifre en az 6 karakter olmalıdır"
                        !hasUpperCase -> "En az bir büyük harf içermelidir"
                        !hasDigit -> "En az bir rakam içermelidir"
                        else -> null
                    }
                    handleDataState { copy(passwordError = error) }
                }
        }
    }

    private fun observeNameValidation() {
        viewModelScope.launch {
            uiDataState.map { it.name }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1200)
                .collect { name ->
                    val error = if (name.isBlank()) "Ad alanı boş bırakılamaz" else null
                    handleDataState { copy(nameError = error) }
                }
        }
    }

    private fun observeSurnameValidation() {
        viewModelScope.launch {
            uiDataState.map { it.surname }
                .distinctUntilChanged()
                .drop(1)
                .debounce(600)
                .collect { surname ->
                    val error = if (surname.isBlank()) "Soyad alanı boş bırakılamaz" else null
                    handleDataState { copy(surnameError = error) }
                }
        }
    }

    public override fun handleIntent(intent: RegisterIntent) {
        when(intent) {
            is RegisterIntent.SetEmail -> {
                handleDataState { copy(email = intent.email, emailError = null) }
            }
            is RegisterIntent.SetPassword -> {
                handleDataState { copy(password = intent.password, passwordError = null) }
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

                    // Email kontrolü
                    viewModelScope.launch {
                        handleDataState { copy(uiState = UiState.Loading) }
                        
                        val emailExists = authRepository.checkEmailExists(email)
                        
                        if (emailExists) {
                            handleDataState {
                                copy(
                                    uiState = UiState.Idle,
                                    isError = true,
                                    errorMessage = "Bu e-posta adresi zaten kayıtlı"
                                )
                            }
                            return@launch
                        }
                        
                        handleDataState {
                            copy(
                                uiState = UiState.Idle,
                                registerStep = intent.registerStep,
                                isError = false,
                                errorMessage = ""
                            )
                        }
                    }
                    return@handleIntent
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
                if (intent.name.length <= 16) {
                    handleDataState { copy(name = intent.name, nameError = null) }
                }
            }
            is RegisterIntent.SetSurname -> {
                if (intent.surname.length <= 16) {
                    handleDataState { copy(surname = intent.surname, surnameError = null) }
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