package com.expense.expensetracking.common.util

sealed class UiState {

    object Loading: UiState()
    object Idle: UiState()
    data class Error(val message: String): UiState()
    data object Success: UiState()
}