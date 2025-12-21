package com.expense.expensetracking.presentation.cards.ui

import com.expense.expensetracking.domain.model.CardItem

sealed class SharedCardIntent {

    data class AddBalance(val value: String): SharedCardIntent()
    data class SetCardName(val name: String): SharedCardIntent()
    data class SetCardBalance(val balance: String): SharedCardIntent()

    data class SetCurrentCard(val card: CardItem): SharedCardIntent()

    data class SetCurrentCardStage(val stage: CardStage): SharedCardIntent()
}