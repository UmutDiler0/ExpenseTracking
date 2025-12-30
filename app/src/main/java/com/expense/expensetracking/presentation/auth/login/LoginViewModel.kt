package com.expense.expensetracking.presentation.auth.login

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.manager.DataStoreManager
import com.expense.expensetracking.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val authRepository: AuthRepository
): BaseViewModel<LoginState, LoginIntent>(
    initialState = LoginState()
)  {

    init {
        observeEmailValidation()
        observePasswordValidation()
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
                .debounce(1200)
                .collect { password ->
                    if (password.isNotEmpty()) {
                        val hasUpperCase = password.any { it.isUpperCase() }
                        val hasDigit = password.any { it.isDigit() }
                        val isLongEnough = password.length >= 6

                        val error = when {
                            !isLongEnough -> "Şifre en az 6 karakter olmalıdır"
                            !hasUpperCase -> "En az bir büyük harf içermelidir"
                            !hasDigit -> "En az bir rakam içermelidir"
                            else -> null
                        }

                        handleDataState { copy(passwordError = error) }
                    } else {
                        handleDataState { copy(passwordError = null) }
                    }
                }
        }
    }

    override public fun handleIntent(intent: LoginIntent) {
        when(intent){
            is LoginIntent.SetEmail -> {
                handleDataState {
                    copy(email = intent.email, emailError = null)
                }
            }
            is LoginIntent.SetPassword -> {
                handleDataState {
                    copy(password = intent.password, passwordError = null)
                }
            }
            is LoginIntent.SetRememberme -> {
                handleDataState {
                    copy(
                        isChecked = !isChecked
                    )
                }
            }
            is LoginIntent.SetPasswordVisibility -> {
                handleDataState {
                    copy(
                        isPasswordVisible = !isPasswordVisible
                    )
                }
            }
            is LoginIntent.ClickLoginBtn -> {
                login()
            }

        }
    }

    private fun saveUserDatastore(){
        viewModelScope.launch {
            if(uiDataState.value.isChecked){
                dataStoreManager.saveRememberMe(true)
                dataStoreManager.saveEmail(uiDataState.value.email)
                dataStoreManager.savePassword(uiDataState.value.password)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            val email = uiDataState.value.email
            val password = uiDataState.value.password

            authRepository.login(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        handleDataState {
                            copy(uiState = UiState.Loading)
                        }
                    }
                    is Resource.Success -> {
                        saveUserDatastore()
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