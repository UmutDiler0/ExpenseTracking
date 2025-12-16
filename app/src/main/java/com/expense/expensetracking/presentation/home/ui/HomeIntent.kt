package com.expense.expensetracking.presentation.home.ui

import android.content.Intent
import com.expense.expensetracking.common.util.Home

sealed class HomeIntent {
    data class AddBalanceValue(val value: String): HomeIntent()
    data class AddSpendValue(val value: String): HomeIntent()
}