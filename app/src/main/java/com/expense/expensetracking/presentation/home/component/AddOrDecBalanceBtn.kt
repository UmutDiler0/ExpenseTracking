package com.expense.expensetracking.presentation.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.ui.theme.BackgroundDark
import com.expense.expensetracking.ui.theme.InputBg
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.TextBlack

@Composable
fun AddOrDecBalanceBtn(
    isAddBalance: Boolean,
    onClick: () -> Unit
){
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isAddBalance) PrimaryGreen else TextBlack
        ),
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = if(isAddBalance) "- Gider Ekle" else "+ Gelir Ekle",
            fontFamily = Manrope,
            color = if(isAddBalance) BackgroundDark else Color.White,
            modifier = Modifier.padding(4.dp)
        )
    }
}