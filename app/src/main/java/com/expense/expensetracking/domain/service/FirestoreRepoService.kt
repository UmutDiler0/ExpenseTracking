package com.expense.expensetracking.domain.service

import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.ExpenseItem
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirestoreRepoService {
    suspend fun addCard(cardItem: CardItem)
    suspend fun removeCard(cardItem: CardItem)
    suspend fun addSpend(expenseItem: ExpenseItem)
    suspend fun addBalance(expenseItem: ExpenseItem)
}