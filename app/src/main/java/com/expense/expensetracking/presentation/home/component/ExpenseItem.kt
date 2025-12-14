package com.expense.expensetracking.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.domain.model.ExpenseItem
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.TextBlack

@Composable
fun ExpenseItem(
    item: ExpenseItem
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Box(
            modifier = Modifier.background(
                color = TextBlack,
                shape = CircleShape
            )
                .size(40.dp),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = item.icon,
                contentDescription = "",
                tint = if(item.isPriceUp) PrimaryGreen else Color.White
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                item.title,
                fontFamily = Manrope,
            )
            Text(
                item.desc,
                fontFamily = Manrope,
                fontWeight = FontWeight.Thin
            )
        }
        Box(modifier = Modifier.weight(1f))
        Text(
            if(item.isPriceUp) "+" + item.price.toString() else "-" + item.price.toString()
        )
    }
}