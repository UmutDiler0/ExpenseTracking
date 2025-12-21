package com.expense.expensetracking.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.data.manager.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class SplashState(
    val isOnBoardingCompleted: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isRememberMe: Boolean = false
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        observeDataStore()
    }

    private fun observeDataStore() {
        combine(
            dataStoreManager.onBoardingCompleted,
            dataStoreManager.email,
            dataStoreManager.password,
            dataStoreManager.isRememberMe
        ) { onBoarding, email, password, rememberMe ->
            SplashState(
                isOnBoardingCompleted = onBoarding,
                email = email,
                password = password,
                isRememberMe = rememberMe
            )
        }.onEach { newState ->
            _state.value = newState
        }.launchIn(viewModelScope)
    }
}