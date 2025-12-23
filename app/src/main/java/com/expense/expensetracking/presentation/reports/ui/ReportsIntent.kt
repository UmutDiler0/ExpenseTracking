package com.expense.expensetracking.presentation.reports.ui

sealed class ReportsIntent {
    data class ChangePeriod(val period: String) : ReportsIntent()
}