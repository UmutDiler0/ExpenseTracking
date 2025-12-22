package com.expense.expensetracking.data.repo

import android.util.Log
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
        val tag = "FirestoreRepo"
        userRef?.let { ref ->
            try {
                Log.e(tag, "Transaction başlatılıyor. Referans: ${ref.path}")

                val finalBalance = firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(ref)
                    if (!snapshot.exists()) {
                        Log.e(tag, "Hata: Firestore dökümanı mevcut değil!")
                        throw Exception("Döküman bulunamadı")
                    }

                    val currentBalance = snapshot.getLong("totalBalance") ?: 0L
                    val newBalance = currentBalance - expenseItem.price

                    Log.e(tag, "Firestore Mevcut Bakiye: $currentBalance, Hesaplanan Yeni Bakiye: $newBalance")

                    transaction.update(ref, "totalBalance", newBalance)
                    transaction.update(ref, "expenseList", FieldValue.arrayUnion(expenseItem))

                    newBalance
                }.await()

                Log.e(tag, "Firestore işlemi başarılı. Yeni Bakiye: $finalBalance")

                val localUser = userDao.getUser()
                if (localUser != null) {
                    Log.e(tag, "Room: Yerel kullanıcı bulundu. Liste güncelleniyor...")
                    val updatedList = localUser.expenseList.toMutableList().apply {
                        add(expenseItem)
                    }

                    userDao.updateUser(
                        localUser.copy(
                            expenseList = updatedList,
                            totalBalance = finalBalance.toInt()
                        )
                    )
                    Log.e(tag, "Room: Kullanıcı ve harcama listesi başarıyla güncellendi.")
                } else {
                    Log.e(tag, "Hata: Room üzerinde yerel kullanıcı bulunamadı (null)!")
                }

            } catch (e: Exception) {
                Log.e(tag, "İşlem sırasında kritik hata: ${e.message}")
                e.printStackTrace()
            }
        } ?: Log.e(tag, "Hata: userRef null! Kullanıcı oturumu kapalı olabilir.")
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