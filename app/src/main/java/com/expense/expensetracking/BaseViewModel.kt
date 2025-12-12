package com.expense.expensetracking

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracking.common.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, INTENT>(
    initialState: STATE
): ViewModel() {

    private val _uiDataState = MutableStateFlow(initialState)
    val uiDataState: StateFlow<STATE> get() = _uiDataState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    protected fun handleDataState(update: STATE.() -> STATE){
        viewModelScope.launch {
            _uiDataState.value = _uiDataState.value.update()
        }
    }

    protected fun handleUiState(uiState: UiState){
        viewModelScope.launch {
            _uiState.value = uiState
        }
    }

    protected abstract fun handleIntent(intent: INTENT)
}