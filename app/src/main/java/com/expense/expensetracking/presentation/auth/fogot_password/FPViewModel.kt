package com.expense.expensetracking.presentation.auth.fogot_password

import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FPViewModel @Inject constructor(
    private val authRepository: AuthRepository
): BaseViewModel<FPState, FPIntent>(
    initialState = FPState()
) {
    public override fun handleIntent(intent: FPIntent) {
        when(intent) {
            is FPIntent.SetEmail -> {
                handleDataState {
                    copy(email = intent.email)
                }
            }
            is FPIntent.Submit -> {
                sendPasswordResetEmail()
            }
        }
    }

    private fun sendPasswordResetEmail() {
        val email = uiDataState.value.email.trim()
        
        if (email.isEmpty()) {
            handleDataState {
                copy(
                    uiState = UiState.Error("Email adresi boş olamaz")
                )
            }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            handleDataState {
                copy(
                    uiState = UiState.Error("Geçerli bir email adresi girin")
                )
            }
            return
        }

        viewModelScope.launch {
            authRepository.sendPasswordResetEmail(email).collect { resource ->
                when (resource) {
                    is Resource.Loading -> handleDataState {
                        copy(
                            uiState = UiState.Loading
                        )
                    }
                    is Resource.Success -> handleDataState {
                        copy(
                            uiState = UiState.Success
                        )

                    }
                    is Resource.Error -> handleDataState {
                        copy(
                            uiState = UiState.Error(resource.message)
                        )

                    }
                    is Resource.Idle -> {
                        handleDataState {
                            copy(
                                uiState = UiState.Idle
                            )
                        }
                    }
                }
            }
        }
    }
}