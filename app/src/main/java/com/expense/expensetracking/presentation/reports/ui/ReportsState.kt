package com.expense.expensetracking.presentation.reports.ui

import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.ExpenseItem

data class ReportsState(
    val uiState: UiState = UiState.Loading,
    val totalExpense: Int = 0,
    val averageExpense: Int = 0,
    val selectedPeriod: String = "Haftalık",
    val categoryDistribution: Map<String, Int> = emptyMap(),
    val weeklyBarData: List<Float> = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)
)