package com.expense.expensetracking.domain.service

import com.expense.expensetracking.common.util.Resource
import com.expense.expensetracking.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepoService {


    fun login(email: String, password: String): Flow<Resource<FirebaseUser>>

    fun register(user: User): Flow<Resource<FirebaseUser>>
}