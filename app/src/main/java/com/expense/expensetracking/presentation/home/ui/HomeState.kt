package com.expense.expensetracking.presentation.home.ui

import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.ExpenseItem
import com.expense.expensetracking.domain.model.User

data class HomeState(
    val uiState: UiState = UiState.Loading,
    val user: User = User(),
    val addBalance: String = "",
    val spendBalance: String = "",
)