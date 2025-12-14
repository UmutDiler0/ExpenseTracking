package com.expense.expensetracking.presentation.auth.fogot_password

import com.expense.expensetracking.common.util.UiState

sealed class FPIntent {
    data class SetEmail(val email: String): FPIntent()
    data object Submit: FPIntent()
}