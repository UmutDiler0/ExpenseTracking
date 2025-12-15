package com.expense.expensetracking.presentation.cards.ui

import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.CardItem


data class SharedCardState(
    val uiState: UiState = UiState.Idle,
    val cardList: List<CardItem> = listOf(),
    val currentCard: CardItem = CardItem(),
    val addCardName: String = "",
    val addCardBalance: String = "",
    val addBalance: String = ""
)