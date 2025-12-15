package com.expense.expensetracking.presentation.cards.ui

sealed class SharedCardIntent {

    data class AddBalance(val value: String): SharedCardIntent()
    data class SetCardName(val name: String): SharedCardIntent()
    data class SetCardBalance(val balance: String): SharedCardIntent()
}