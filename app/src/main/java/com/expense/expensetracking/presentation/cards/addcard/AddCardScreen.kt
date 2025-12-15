package com.expense.expensetracking.presentation.cards.addcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.common.component.AddCardTextField
import com.expense.expensetracking.common.component.CustomTopAppBar

@Composable
fun AddCardScreen(
    onPopBackStack: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(
            icon = Icons.Rounded.Notifications,
            header = "Kart Ekle",
            isBackBtnActive = true,
            isTrailingIconActive = false
        ) {
            onPopBackStack()
        }
        AddCardTextField(
            header = "Kart Adı",
            value = "",
            hint = "Kart Adını Giriniz"
        ) { }
        AddCardTextField(
            header = "Bakiye",
            value = "",
            hint = "Bakiyenizi Giriniz"
        ) { }
    }
}