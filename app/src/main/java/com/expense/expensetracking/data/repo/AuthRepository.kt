package com.expense.expensetracking.data.repo

import android.util.Log
import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.data.manager.DataStoreManager
import com.expense.expensetracking.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao,
    private val dataStoreManager: DataStoreManager,
) {

    val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()
    val currentUser = auth.currentUser

    fun login(email: String, pass: String): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading)
        try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            if (result.user != null) {
                syncUserToLocal()
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
                    "totalBalance" to user.totalBalance,
                    "createdAt" to com.google.firebase.Timestamp.now()
                )

                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(userMap)
                    .await()

                userDao.insertUser(
                    user.copy(
                        email = user.email,
                        name = user.name,
                        surname = user.surname
                    )
                )

                emit(Resource.Success(firebaseUser))
            } else {
                emit(Resource.Error("Kayıt oluşturulamadı."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Kayıt sırasında hata oluştu"))
        }
    }

    suspend fun syncUserToLocal() {
        try {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val snapshot = firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()

                val user = snapshot.toObject(User::class.java)

                if (user != null) {
                    userDao.insertUser(user)
                }
            }
        } catch (e: Exception) {
            println("User sync error: ${e.message}")
        }
    }

    suspend fun logout() {
        try {
            auth.signOut()
            userDao.deleteUser()
            dataStoreManager.clearAuthData()
        } catch (e: Exception) {
            Log.e("AuthRepository", e.message ?: "Logout Error")
        }
    }

    fun sendPasswordResetEmail(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Şifre sıfırlama emaili gönderilemedi"))
        }
    }

    suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val signInMethods = auth.fetchSignInMethodsForEmail(email).await()
            !signInMethods.signInMethods.isNullOrEmpty()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Email kontrol hatası: ${e.message}")
            false
        }
    }
}