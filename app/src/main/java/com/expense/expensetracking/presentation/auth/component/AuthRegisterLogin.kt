package com.expense.expensetracking.presentation.auth.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.ui.theme.PrimaryGreen

@Composable
fun AuthRegisterLogin(
    first: String,
    second: String,
    onClicl: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            first,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Text(
            second,
            color = PrimaryGreen,
            modifier = Modifier.clickable{
                onClicl()
            },
            fontSize = 12.sp
        )
    }
}