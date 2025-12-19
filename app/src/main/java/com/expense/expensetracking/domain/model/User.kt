package com.expense.expensetracking.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val email: String = "",

    val password: String = "",
    val name: String = "",
    val surname: String = "",
    val totalBalance: Int = 0,
    val expenseList: List<ExpenseItem> = listOf(),
    val cardList: List<CardItem> = listOf(),
)