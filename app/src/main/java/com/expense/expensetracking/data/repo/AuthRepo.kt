package com.expense.expensetracking.data.repo

import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.domain.model.User
import com.expense.expensetracking.domain.service.AuthRepoService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    fun login(email: String, pass: String): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading)
        try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            if (result.user != null) {
                emit(Resource.Success(result.user!!))
            } else {
                emit(Resource.Error("Kullanıcı bulunamadı."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Giriş yapılamadı"))
        }
    }

    fun register(user: User): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading)
        try {

            val authResult = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {

                val userMap = hashMapOf(
                    "uid" to firebaseUser.uid,
                    "email" to user.email,
                    "name" to user.name,
                    "surname" to user.surname,
                    "expenseList" to user.expenseList,
                    "cardList" to user.cardList,
                    "createdAt" to com.google.firebase.Timestamp.now()
                )


                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(userMap)
                    .await()

                emit(Resource.Success(firebaseUser))
            } else {
                emit(Resource.Error("Kayıt oluşturulamadı."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Kayıt sırasında hata oluştu"))
        }
    }
}