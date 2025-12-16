package com.expense.expensetracking.presentation.home.ui

import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.ExpenseItem

data class HomeState(
    val uiState: UiState = UiState.Idle,
    val totalBalance: String = "",
    val expenseList: List<ExpenseItem> = listOf(),
    val addBalance: String = "",
    val spendBalance: String = "",
)