package com.expense.expensetracking.common.component

import android.R
import android.provider.CalendarContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
        modifier = Modifier
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
            onValueChange = { newValue ->
                if (header == "Bakiye") {
                    if (newValue.all { it.isDigit() }) {
                        onTextValueChange(newValue)
                    }
                } else {
                    onTextValueChange(newValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (header == "Bakiye") KeyboardType.Number else KeyboardType.Text
            ),
            placeholder = {
                Text(
                    hint,
                    color = Color.DarkGray,
                    fontSize = 20.sp
                )
            },
            colors = TextFieldDefaults.colors(
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