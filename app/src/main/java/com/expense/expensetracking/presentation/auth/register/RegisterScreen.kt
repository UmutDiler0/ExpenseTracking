package com.expense.expensetracking.presentation.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.presentation.auth.component.AuthEditText
import com.expense.expensetracking.presentation.auth.component.AuthHeader
import com.expense.expensetracking.presentation.auth.component.AuthRegisterLogin

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateHomeScreen: () -> Unit,
    onNavigateLoginScreen: () -> Unit
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by viewModel.uiDataState.collectAsState()

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
                hint = "Email adresinizi girin",
                isPassword = false,
                value = state.email,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onValueChange = { newEmail ->
                    viewModel.handleIntent(RegisterIntent.SetEmail(newEmail))
                }
            )

            AuthEditText(
                label = "Password",
                hint = "Şifrenizi belirleyin",
                isPassword = true,
                visible = state.isPasswordVisible,
                value = state.password,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,

                onClick = {
                    viewModel.handleIntent(RegisterIntent.SetPasswordVisiblity)
                },

                onImeAction = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },

                onValueChange = { newPassword ->
                    viewModel.handleIntent(RegisterIntent.SetPassword(newPassword))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppBtn("Kayıt Ol") {
                onNavigateHomeScreen()
            }
        }
        AuthRegisterLogin(
            "Hesabın var mı?",
            "Giriş Yap"
        ) { onNavigateLoginScreen() }
    }
}