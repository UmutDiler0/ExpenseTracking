package com.expense.expensetracking.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.data.manager.DataStoreManager
import com.expense.expensetracking.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashState(
    val isOnBoardingCompleted: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isRememberMe: Boolean = false
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        observeAndAutoLogin()
    }

    private fun observeAndAutoLogin() {
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

            // Veriler ilk kez yüklendiğinde ve rememberMe aktifse login başlat
            if (newState.isRememberMe && newState.email.isNotBlank() && newState.password.isNotBlank()) {
                performAutoLogin(newState.email, newState.password)
            }
        }.launchIn(viewModelScope)
    }

    private fun performAutoLogin(email: String, pass: String) {
        viewModelScope.launch {
            authRepository.login(email, pass).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        // Giriş başarılı, state'i veya navigasyon tetikleyiciyi güncelle
                    }
                    is Resource.Error -> {
                        // Giriş başarısız, belki isRememberMe'yi false yapmalısın
                    }
                    is Resource.Loading -> { }
                    is Resource.Idle -> {}
                }
            }
        }
    }
}