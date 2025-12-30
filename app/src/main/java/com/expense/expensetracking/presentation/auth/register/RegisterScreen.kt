package com.expense.expensetracking.presentation.auth.register

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.R
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.Register
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
            stringResource(R.string.register_title),
            stringResource(R.string.register_subtitle)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.registerStep == RegisterStep.EMAIL_PASSWORD) {
                    Column {
                        AuthEditText(
                            label = stringResource(R.string.register_email_label),
                            hint = stringResource(R.string.register_email_hint),
                            isPassword = false,
                            value = state.email,
                            isError = state.emailError != null,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                            onValueChange = { viewModel.handleIntent(RegisterIntent.SetEmail(it)) }
                        )
                        AnimatedVisibility(visible = state.emailError != null) {
                            Text(
                                text = state.emailError ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Column {
                        AuthEditText(
                            label = stringResource(R.string.register_password_label),
                            hint = stringResource(R.string.register_password_hint),
                            isPassword = true,
                            visible = state.isPasswordVisible,
                            value = state.password,
                            isError = state.passwordError != null,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                            onClick = { viewModel.handleIntent(RegisterIntent.SetPasswordVisiblity) },
                            onImeAction = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            onValueChange = { viewModel.handleIntent(RegisterIntent.SetPassword(it)) }
                        )
                        AnimatedVisibility(visible = state.passwordError != null) {
                            Text(
                                text = state.passwordError ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            viewModel.handleIntent(RegisterIntent.SetRegisterStep(RegisterStep.EMAIL_PASSWORD))
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.register_back))
                        }
                        Text(text = stringResource(R.string.register_personal_info), style = MaterialTheme.typography.titleMedium)
                    }

                    Column {
                        AuthEditText(
                            label = stringResource(R.string.register_name_label),
                            hint = stringResource(R.string.register_name_hint),
                            isPassword = false,
                            value = state.name,
                            isError = state.nameError != null,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            onValueChange = { viewModel.handleIntent(RegisterIntent.SetName(it)) }
                        )
                        AnimatedVisibility(visible = state.nameError != null) {
                            Text(
                                text = state.nameError ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Column {
                        AuthEditText(
                            label = stringResource(R.string.register_surname_label),
                            hint = stringResource(R.string.register_surname_hint),
                            isPassword = false,
                            value = state.surname,
                            isError = state.surnameError != null,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            onImeAction = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            onValueChange = { viewModel.handleIntent(RegisterIntent.SetSurname(it)) }
                        )
                        AnimatedVisibility(visible = state.surnameError != null) {
                            Text(
                                text = state.surnameError ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            AppBtn(stringResource(R.string.register_button)) {
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
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

        }
        AuthRegisterLogin(
            stringResource(R.string.register_has_account),
            stringResource(R.string.register_login)
        ) { onNavigateLoginScreen() }
    }
}