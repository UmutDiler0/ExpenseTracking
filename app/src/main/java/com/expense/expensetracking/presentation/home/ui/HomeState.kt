package com.expense.expensetracking.presentation.home.ui

import androidx.benchmark.traceprocessor.Insight
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.Category
import com.expense.expensetracking.domain.model.ExpenseItem
import com.expense.expensetracking.domain.model.User
import com.expense.expensetracking.presentation.home.component.Menu

data class HomeState(
    val uiState: UiState = UiState.Loading,
    val user: User = User(),
    val addBalance: String = "",
    val spendBalance: String = "",
    val selectedCardItem: CardItem = CardItem(),
    val selectedCategory: Category = Category("",""),
    val currentMenuState: Menu = Menu.IDLE
)