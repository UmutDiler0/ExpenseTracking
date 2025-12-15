package com.expense.expensetracking.presentation.cards.component

import android.view.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.ui.theme.BackgroundDark
import com.expense.expensetracking.ui.theme.InputBg
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark
import com.expense.expensetracking.ui.theme.TextBlack

@Composable
fun CardScreenItem(
    item: CardItem,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable{
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.CreditCard,
                contentDescription = ""
            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    item.name,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    item.balance.toString(),
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Thin
                )
            }
            Box(modifier = Modifier.weight(1f))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = InputBg,
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Yükle", color = PrimaryGreen)
            }
        }
    }
}