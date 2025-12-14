package com.expense.expensetracking.presentation.home.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.common.util.tempList
import com.expense.expensetracking.ui.theme.Manrope

@Composable
fun ExpensesList(

){
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
    ) {
        item {
            Text(
                "Son Harcamalar",
                fontFamily = Manrope,
                fontWeight = FontWeight.SemiBold,
            )
        }
        items(tempList){item ->
            ExpenseItem(item)
        }
    }
}