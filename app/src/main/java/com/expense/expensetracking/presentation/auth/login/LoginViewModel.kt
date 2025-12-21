package com.expense.expensetracking.presentation.auth.login

import androidx.datastore.dataStore
import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.manager.DataStoreManager
import com.expense.expensetracking.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val authRepository: AuthRepository
): BaseViewModel<LoginState, LoginIntent>(
    initialState = LoginState()
)  {
    override public fun handleIntent(intent: LoginIntent) {
        when(intent){
           is LoginIntent.SetEmail -> {
               handleDataState {
                   copy(
                       email = intent.email
                   )
               }
           }
            is LoginIntent.SetPassword -> {
                handleDataState {
                    copy(
                        password = intent.password
                    )
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