package com.expense.expensetracking.presentation.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.auth.component.AuthEditText
import com.expense.expensetracking.presentation.auth.component.AuthHeader
import com.expense.expensetracking.presentation.auth.component.AuthRegisterLogin
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateRegisterScreen: () -> Unit,
    onNavigateHomeScreen: () -> Unit,
    onNavigateForgotPassword: () -> Unit
) {
    val state by viewModel.uiDataState.collectAsState()

    when (state.uiState) {
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Success -> {
            LaunchedEffect(Unit) {
                onNavigateHomeScreen()
            }
        }
        else -> {
            LoginIdle(
                viewModel = viewModel,
                state = state,
                onNavigateRegisterScreen = onNavigateRegisterScreen,
                onNavigateHomeScreen = onNavigateHomeScreen,
                onNavigateForgotPassword = onNavigateForgotPassword
            )
        }
    }
}

@Composable
fun LoginIdle(
    viewModel: LoginViewModel,
    state: LoginState,
    onNavigateRegisterScreen: () -> Unit,
    onNavigateHomeScreen: () -> Unit,
    onNavigateForgotPassword: () -> Unit
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
            "Tekrar Hoş Geldin!",
            "Devam etmek için giriş yap"
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuthEditText(
                label = "Email",
                hint = "Email",
                isPassword = false,
                value = state.email,
                isError = state.isError,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onValueChange = {
                    viewModel.handleIntent(LoginIntent.SetEmail(it))
                }
            )
            AuthEditText(
                label = "Şifre",
                hint = "Şifre",
                isPassword = true,
                isError = state.isError,
                visible = state.isPasswordVisible,
                value = state.password,
                onClick = {
                    viewModel.handleIntent(LoginIntent.SetPasswordVisibility)
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onImeAction = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onValueChange = {
                    viewModel.handleIntent(LoginIntent.SetPassword(it))
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Beni Hatırla", fontFamily = Manrope, fontSize = 12.sp)
                    Checkbox(
                        checked = state.isChecked,
                        onCheckedChange = {
                            viewModel.handleIntent(LoginIntent.SetRememberme)
                        }
                    )
                }
                Text(
                    "Şifremi Unuttum?",
                    fontFamily = Manrope,
                    color = PrimaryGreen,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {
                        onNavigateForgotPassword()
                    }
                )
            }
            AppBtn("Giriş Yap") {
                viewModel.handleIntent(LoginIntent.ClickLoginBtn)
            }
            if(state.isError){
                Text(
                    state.errorMessage,
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Manrope
                )
            }
        }
        AuthRegisterLogin(
            "Hesabın yok mu?",
            "Hesap Oluştur"
        ) {
            onNavigateRegisterScreen()
        }
    }
}