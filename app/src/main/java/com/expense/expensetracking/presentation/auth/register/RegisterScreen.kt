package com.expense.expensetracking.presentation.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.auth.component.AuthEditText
import com.expense.expensetracking.presentation.auth.component.AuthHeader
import com.expense.expensetracking.presentation.auth.component.AuthRegisterLogin
import com.expense.expensetracking.ui.theme.Manrope

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateHomeScreen: () -> Unit,
    onNavigateLoginScreen: () -> Unit
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
            RegisterIdle(
                viewModel,
                state,
                onNavigateHomeScreen
            ) {
                onNavigateLoginScreen()
            }
        }
    }
}

@Composable
fun RegisterIdle(
    viewModel: RegisterViewModel,
    state: RegisterState,
    onNavigateHomeScreen: () -> Unit,
    onNavigateLoginScreen: () -> Unit
) {
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
            if(state.registerStep == RegisterStep.EMAIL_PASSWORD){
                AuthEditText(
                    label = "Email",
                    hint = "Email adresinizi girin",
                    isPassword = false,
                    value = state.email,
                    isError = state.isError,
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
                    isError = state.isError,
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
            }else{
                AuthEditText(
                    label = "Adınız",
                    hint = "Adınızı Yazınız",
                    isPassword = false,
                    visible = state.isPasswordVisible,
                    value = state.name,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,

                    onClick = {
                        viewModel.handleIntent(RegisterIntent.SetPasswordVisiblity)
                    },

                    onImeAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },

                    onValueChange = { name ->
                        viewModel.handleIntent(RegisterIntent.SetName(name))
                    }
                )
                AuthEditText(
                    label = "Soyadınız",
                    hint = "Soyadınızı giriniz",
                    isPassword = false,
                    visible = state.isPasswordVisible,
                    value = state.surname,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,

                    onClick = {
                        viewModel.handleIntent(RegisterIntent.SetPasswordVisiblity)
                    },

                    onImeAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },

                    onValueChange = { surname ->
                        viewModel.handleIntent(RegisterIntent.SetSurname(surname))
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            AppBtn("Kayıt Ol") {
                if(state.registerStep == RegisterStep.NAME_SURNAME){
                    viewModel.handleIntent(RegisterIntent.OnClickRegisterBtn)
                }else{
                    viewModel.handleIntent(RegisterIntent.SetRegisterStep(RegisterStep.NAME_SURNAME))
                }
            }
            if(state.isError){
                Text(
                    state.errorMessage,
                    color = Color.Red,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }
        AuthRegisterLogin(
            "Hesabın var mı?",
            "Giriş Yap"
        ) { onNavigateLoginScreen() }
    }
}