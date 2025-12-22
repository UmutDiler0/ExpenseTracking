package com.expense.expensetracking.data.repo

import android.util.Log
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.ExpenseItem
import com.expense.expensetracking.domain.model.User
import com.expense.expensetracking.domain.service.FirestoreRepoService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

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
        val tag = "FirestoreRepo"
        userRef?.let { ref ->
            try {
                Log.e(tag, "Transaction başlatılıyor...")

                val updatedUserData = firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(ref)
                    if (!snapshot.exists()) throw Exception("Döküman bulunamadı")

                    val currentBalance = snapshot.getLong("totalBalance") ?: 0L
                    val firestoreCardList = snapshot.toObject(User::class.java)?.cardList ?: emptyList()

                    val newTotalBalance = currentBalance - expenseItem.price

                    val updatedCardList = firestoreCardList.map { card ->
                        if (card.name == expenseItem.spendOrAddCard.name) {
                            card.copy(balance = card.balance - expenseItem.price)
                        } else {
                            card
                        }
                    }

                    transaction.update(ref, "totalBalance", newTotalBalance)
                    transaction.update(ref, "cardList", updatedCardList)
                    transaction.update(ref, "expenseList", FieldValue.arrayUnion(expenseItem))

                    Pair(newTotalBalance, updatedCardList)
                }.await()

                Log.e(tag, "Firestore başarılı. Room güncelleniyor...")

                val localUser = userDao.getUser()
                localUser?.let { user ->
                    val updatedExpenseList = user.expenseList.toMutableList().apply {
                        add(expenseItem)
                    }

                    userDao.updateUser(
                        user.copy(
                            expenseList = updatedExpenseList,
                            totalBalance = updatedUserData.first.toInt(),
                            cardList = updatedUserData.second
                        )
                    )
                    Log.e(tag, "Room ve Kart bakiyesi başarıyla güncellendi.")
                }

            } catch (e: Exception) {
                Log.e(tag, "Hata: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    override suspend fun addBalance(expenseItem: ExpenseItem) {
        val tag = "FirestoreRepo"
        userRef?.let { ref ->
            try {
                val updatedData = firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(ref)
                    if (!snapshot.exists()) throw Exception("Döküman bulunamadı")

                    val currentTotalBalance = snapshot.getLong("totalBalance") ?: 0L
                    val firestoreCardList = snapshot.toObject(User::class.java)?.cardList ?: emptyList()

                    val newTotalBalance = currentTotalBalance + expenseItem.price

                    val updatedCardList = firestoreCardList.map { card ->
                        if (card.name == expenseItem.spendOrAddCard.name) {
                            card.copy(balance = card.balance + expenseItem.price)
                        } else {
                            card
                        }
                    }

                    transaction.update(ref, "totalBalance", newTotalBalance)
                    transaction.update(ref, "cardList", updatedCardList)
                    transaction.update(ref, "expenseList", FieldValue.arrayUnion(expenseItem))

                    Pair(newTotalBalance, updatedCardList)
                }.await()

                val localUser = userDao.getUser()
                localUser?.let { user ->
                    val updatedExpenseList = user.expenseList.toMutableList().apply {
                        add(expenseItem)
                    }

                    userDao.updateUser(
                        user.copy(
                            expenseList = updatedExpenseList,
                            totalBalance = updatedData.first.toInt(),
                            cardList = updatedData.second
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(tag, "Bakiye ekleme hatası: ${e.message}")
            }
        }
    }
}