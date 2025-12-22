package com.expense.expensetracking.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.expense.expensetracking.common.util.IconFactory
import com.expense.expensetracking.common.util.formatTimestamp

import com.expense.expensetracking.domain.model.ExpenseItem
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.TextBlack

@Composable
fun ExpenseItem(
    item: ExpenseItem
){
     val date = formatTimestamp(item.spendDate)
    item.iconName
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
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
                imageVector = IconFactory.getIcon(item.iconName),
                contentDescription = "",
                tint = if(item.priceUp) PrimaryGreen else Color.White
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                item.title,
                fontFamily = Manrope,
            )
            Text(
                date,
                fontFamily = Manrope,
                fontWeight = FontWeight.Thin
            )
        }
        Box(modifier = Modifier.weight(1f))
        Text(
            if(item.priceUp) "+" + item.price.toString() else "-" + item.price.toString(),

        )
    }
}