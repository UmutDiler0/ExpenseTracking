package com.expense.expensetracking.presentation.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.presentation.auth.component.AuthEditText
import com.expense.expensetracking.presentation.auth.component.AuthHeader
import com.expense.expensetracking.presentation.auth.component.AuthRegisterLogin
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen

@Composable
fun RegisterScreen(
    onNavigateHomeScreen: () -> Unit,
    onNavigateLoginScreen: () -> Unit
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        AuthHeader(
            "Hoş Geldin!",
            "Devam etmek için kayıt ol"
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuthEditText(
                label = "Email",
                hint = "Email",
                isPassword = false,
                value = "",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onValueChange = { }
            )
            AuthEditText(
                label = "Password",
                hint = "Password",
                isPassword = true,
                value = "",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onImeAction = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onValueChange = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppBtn("Kayıt Ol") { }
        }
        AuthRegisterLogin(
            "Hesabın yok mu?",
            "Hesap Oluştur"
        ) { onNavigateLoginScreen() }
    }
}