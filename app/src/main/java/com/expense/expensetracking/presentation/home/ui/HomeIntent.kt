package com.expense.expensetracking.presentation.home.ui

import android.content.Intent
import com.expense.expensetracking.common.util.Home
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.Category
import com.expense.expensetracking.presentation.home.component.Menu

sealed class HomeIntent {
    data class AddBalanceValue(val value: String): HomeIntent()
    data class AddSpendValue(val value: String): HomeIntent()

    data class SelectCategory(val category: Category): HomeIntent()

    data class SelectCard(val card: CardItem): HomeIntent()

    data class SetMenu(val menu: Menu): HomeIntent()
}