package com.expense.expensetracking.presentation.auth.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.TextBlack
import com.expense.expensetracking.ui.theme.TextGray
import com.expense.expensetracking.ui.theme.TextWhite

@Composable
fun AuthEditText(
    label: String,
    hint: String,
    isPassword: Boolean,
    visible: Boolean = false,
    value: String,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit
) {
    val shakeOffset = remember { Animatable(0f) }

    // Temaya göre renkleri dinamik seçiyoruz
    val errorColor = MaterialTheme.colorScheme.error
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val iconTint = if (isError) errorColor else MaterialTheme.colorScheme.primary

    LaunchedEffect(isError) {
        if (isError) {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0f at 0
                    (-10f) at 50
                    10f at 100
                    (-10f) at 150
                    10f at 200
                    (-5f) at 250
                    5f at 300
                    0f at 400
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(x = shakeOffset.value.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isError) errorColor else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            leadingIcon = {
                Icon(
                    imageVector = if (isPassword) Icons.Filled.Lock else Icons.Filled.Person,
                    contentDescription = null,
                    tint = iconTint
                )
            },
            colors = TextFieldDefaults.colors(
                // Arka plan renkleri
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                errorContainerColor = containerColor,

                // Yazı renkleri
                focusedTextColor = contentColor,
                unfocusedTextColor = contentColor,
                errorTextColor = contentColor,

                // Çizgileri tamamen gizliyoruz (Modern görünüm için)
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,

                // İmleç ve ikon renkleri
                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = errorColor,
                errorLeadingIconColor = errorColor,
                errorTrailingIconColor = errorColor
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = when {
                        isError -> errorColor
                        else -> Color.Transparent
                    },
                    shape = RoundedCornerShape(12.dp)
                ),
            placeholder = {
                Text(
                    text = hint,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isError) errorColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction() },
                onNext = { onImeAction() }
            ),
            singleLine = true,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = if (visible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = null,
                            tint = if (isError) errorColor else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
