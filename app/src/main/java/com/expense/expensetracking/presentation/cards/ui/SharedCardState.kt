package com.expense.expensetracking.presentation.cards.ui

import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.User


data class SharedCardState(
    val uiState: UiState = UiState.Loading,
    val user: User = User(),
    val cardList: List<CardItem> = listOf(),
    val currentCard: CardItem = CardItem(),
    val addCardName: String = "",
    val addCardBalance: String = "",
    val addBalance: String = ""
)