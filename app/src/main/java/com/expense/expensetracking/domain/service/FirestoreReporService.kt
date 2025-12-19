package com.expense.expensetracking.domain.service

import com.expense.expensetracking.common.util.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirestoreReporService {
    fun getUser(): Flow<Resource<FirebaseUser>>
}