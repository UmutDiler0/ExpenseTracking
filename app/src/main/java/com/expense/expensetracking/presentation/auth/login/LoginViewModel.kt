package com.expense.expensetracking.presentation.auth.login

import com.expense.expensetracking.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): BaseViewModel<LoginState, LoginIntent>(
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
        }
    }
}