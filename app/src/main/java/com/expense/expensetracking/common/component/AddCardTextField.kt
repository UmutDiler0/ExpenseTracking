package com.expense.expensetracking.common.component

import android.R
import android.provider.CalendarContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.ui.theme.Manrope

@Composable
fun AddCardTextField(
    header: String,
    value: String,
    hint: String,
    onTextValueChange: (String) -> Unit
){
    Column(
        modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        Text(
            header,
            fontSize = 16.sp,
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            value = value,
            onValueChange = { onTextValueChange(it) },
            modifier = Modifier.fillMaxWidth(),

            // 1. Köşelere 12.dp radius veriyoruz
            shape = RoundedCornerShape(12.dp),

            placeholder = {
                Text(
                    hint,
                    color = Color.DarkGray,
                    fontSize = 20.sp
                )
            },

            // 2. Alt çizgiyi kaldırmak için renk ayarlarını yapıyoruz
            colors = TextFieldDefaults.colors(
                // Alt çizgiyi (Border/Indicator) görünmez yapıyoruz
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,

                cursorColor = Color.White,

                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}