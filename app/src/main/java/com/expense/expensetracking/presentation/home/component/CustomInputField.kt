package com.expense.expensetracking.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.ui.theme.PrimaryGreen

@Composable
fun CustomInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isNumeric: Boolean,
    isSingleLine: Boolean,
    minHeight: androidx.compose.ui.unit.Dp = 64.dp,
    backgroundColor: Color,
    textColor: Color,
    labelColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = labelColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            singleLine = isSingleLine,
            textStyle = TextStyle(
                fontSize = if (isNumeric) 24.sp else 16.sp,
                fontWeight = if (isNumeric) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            ),
            cursorBrush = SolidColor(PrimaryGreen),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = minHeight)
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor)
                        .padding(16.dp),
                    contentAlignment = if(isSingleLine) Alignment.CenterStart else Alignment.TopStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                fontSize = if (isNumeric) 24.sp else 16.sp,
                                fontWeight = if (isNumeric) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSystemInDarkTheme()) Color(0xFF6B7280) else Color(
                                    0xFF9CA3AF
                                )
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}