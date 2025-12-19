package com.expense.expensetracking.common.util

import androidx.room.TypeConverter
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.ExpenseItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // ExpenseItem Listesi Dönüştürücüleri
    @TypeConverter
    fun fromExpenseList(value: List<ExpenseItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toExpenseList(value: String): List<ExpenseItem> {
        val listType = object : TypeToken<List<ExpenseItem>>() {}.type
        return gson.fromJson(value, listType)
    }

    // CardItem Listesi Dönüştürücüleri
    @TypeConverter
    fun fromCardList(value: List<CardItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCardList(value: String): List<CardItem> {
        val listType = object : TypeToken<List<CardItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}