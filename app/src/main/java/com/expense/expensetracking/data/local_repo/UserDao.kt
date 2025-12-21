package com.expense.expensetracking.data.local_repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.ExpenseItem
import com.expense.expensetracking.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): User?

    @Query("DELETE FROM users")
    suspend fun deleteUser()

    @Query("SELECT * FROM users LIMIT 1")
    fun observeUser(): Flow<User?>

    @Update
    suspend fun updateUser(user: User)


    @Query("UPDATE users SET totalBalance = :newBalance")
    suspend fun updateTotalBalance(newBalance: Int)

    @Query("UPDATE users SET expenseList = :newList")
    suspend fun updateExpenseList(newList: List<ExpenseItem>)

    @Query("UPDATE users SET cardList = :newList")
    suspend fun updateCardList(newList: List<CardItem>)
}