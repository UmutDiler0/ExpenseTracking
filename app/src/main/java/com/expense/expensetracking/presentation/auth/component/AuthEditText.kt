package com.expense.expensetracking.presentation.auth.component

import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.expense.expensetracking.ui.theme.InputBg
import com.expense.expensetracking.ui.theme.InputText

import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.TextWhite

@Composable
fun AuthEditText(
    label: String,
    hint: String,
    isPassword: Boolean,
    value: String,
    onValueChange: (String) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text(
            label,
            fontFamily = Manrope,
        )
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            leadingIcon = {
                if (isPassword) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "",
                        tint = InputText
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription =  null,
                        tint = InputText
                    )
                }

            },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBg,
                unfocusedContainerColor = InputBg,
                disabledContainerColor = InputBg,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = TextWhite,
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            placeholder = {
                Text(
                    hint,
                )
            }
        )
    }
}
