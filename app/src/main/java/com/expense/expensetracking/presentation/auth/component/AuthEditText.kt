package com.expense.expensetracking.presentation.auth.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    visible: Boolean = false,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
                        contentDescription = null,
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
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    hint,
                    maxLines = 1
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onImeAction()
                },
                onNext = {
                    onImeAction()
                }
            ),
            singleLine = true,
            trailingIcon = {
                if(isPassword){
                    Icon(
                        imageVector = if(visible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        contentDescription = "",
                        modifier = Modifier.clickable{
                            onClick()
                        }
                    )
                }
            },
            visualTransformation = if (isPassword && !visible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
        )
    }
}
