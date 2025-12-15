package com.expense.expensetracking.presentation.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.presentation.home.component.AddOrDecBalanceBtn
import com.expense.expensetracking.presentation.home.component.BalanceCard
import com.expense.expensetracking.presentation.home.component.ExpensesList

@Composable
fun HomeScreen(

){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        CustomTopAppBar(
            icon = Icons.Rounded.Notifications,
            header = "Ana Ekran",
            isBackBtnActive = false,
            isTrailingIconActive = true
        ){}
        BalanceCard()
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AddOrDecBalanceBtn(true) { }
            AddOrDecBalanceBtn(false) { }
        }
        Spacer(modifier = Modifier.height(32.dp))
        ExpensesList()
    }
}