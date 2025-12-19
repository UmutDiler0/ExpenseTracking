package com.expense.expensetracking

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.expense.expensetracking.common.util.Converters
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.domain.model.User

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
}