package com.expense.expensetracking.domain.model

data class User(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val surname: String,
    val expenseList: List<ExpenseItem> = listOf(),
    val cardList: List<CardItem> = listOf(),
)