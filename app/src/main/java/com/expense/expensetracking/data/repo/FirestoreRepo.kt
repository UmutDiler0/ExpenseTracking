package com.expense.expensetracking.data.repo

import androidx.compose.animation.core.snap
import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.ExpenseItem
import com.expense.expensetracking.domain.model.User
import com.expense.expensetracking.domain.service.FirestoreRepoService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.jvm.java

class FirestoreRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val userDao: UserDao
) : FirestoreRepoService {

    private val userRef: DocumentReference?
        get() = auth.currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid)
        }

    override suspend fun addCard(cardItem: CardItem) {
        userRef?.update(
            "cardList", FieldValue.arrayUnion(cardItem),
            "totalBalance", FieldValue.increment(cardItem.balance.toLong())
        )?.await()

        val localUser = userDao.getUser()
        localUser?.let {
            val updatedList = it.cardList.toMutableList().apply { add(cardItem) }
            val updatedTotalBalance = it.totalBalance + cardItem.balance

            userDao.updateUser(
                it.copy(
                    cardList = updatedList,
                    totalBalance = updatedTotalBalance
                )
            )
        }
    }

    override suspend fun removeCard(cardItem: CardItem) {
        val snapshot = userRef?.get()?.await()
        val firestoreUser = snapshot?.toObject(User::class.java)

        if (firestoreUser != null && firestoreUser.cardList.isNotEmpty()) {
            userRef?.update("cardList", FieldValue.arrayRemove(cardItem))?.await()

            val localUser = userDao.getUser()
            localUser?.let {
                val updatedList = it.cardList.toMutableList().apply { remove(cardItem) }
                userDao.updateCardList(updatedList)
            }
        }
    }

    override suspend fun addSpend(expenseItem: ExpenseItem) {
        firestore.runTransaction { transaction ->
            val snapshot = userRef?.let { transaction.get(it) }
            val currentBalance = snapshot?.getLong("totalBalance") ?: 0L
            val newBalance = (currentBalance - expenseItem.price).toInt()

            userRef?.let {
                transaction.update(it, "totalBalance", newBalance)
                transaction.update(it, "expenseList", FieldValue.arrayUnion(expenseItem))
            }
            newBalance
        }.await().also { newBalance ->
            val localUser = userDao.getUser()
            localUser?.let {
                val updatedList = it.expenseList.toMutableList().apply { add(expenseItem) }
                userDao.updateExpenseList(updatedList)
                userDao.updateTotalBalance(newBalance.toInt())
            }
        }
    }

    override suspend fun addBalance(expenseItem: ExpenseItem) {
        firestore.runTransaction { transaction ->
            val snapshot = userRef?.let { transaction.get(it) }
            val currentBalance = snapshot?.getLong("totalBalance") ?: 0L
            val newBalance = (currentBalance + expenseItem.price).toInt()

            userRef?.let {
                transaction.update(it, "totalBalance", newBalance)
                transaction.update(it, "expenseList", FieldValue.arrayUnion(expenseItem))
            }
            newBalance
        }.await().also { newBalance ->
            val localUser = userDao.getUser()
            localUser?.let {
                val updatedList = it.expenseList.toMutableList().apply { add(expenseItem) }
                userDao.updateExpenseList(updatedList)
                userDao.updateTotalBalance(newBalance.toInt())
            }
        }
    }
}