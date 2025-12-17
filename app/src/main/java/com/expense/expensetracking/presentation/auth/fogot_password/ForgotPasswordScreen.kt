package com.expense.expensetracking.presentation.auth.fogot_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.BackBtn
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.auth.component.AuthEditText
import com.expense.expensetracking.presentation.auth.component.AuthHeader

@Composable
fun ForgotPasswordScreen(
    viewModel: FPViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit
){
    val state by viewModel.uiDataState.collectAsStateWithLifecycle()
    when(state.uiState){
        is UiState.Idle -> {
            ForgotPasswordIdleScreen(viewModel, state){
                onPopBackStack()
            }
        }
        is UiState.Error -> {

        }
        is UiState.Success -> {}
        is UiState.Loading -> {
            LoadingScreen()
        }
    }

}

@Composable
fun ForgotPasswordIdleScreen(
    viewModel: FPViewModel,
    state: FPState,
    onPopBackStack: () -> Unit
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding() // Klavye açılınca yukarı kayması için
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            BackBtn {
                onPopBackStack()
            }
            AuthHeader(
                header = "Şifremi Unuttum",
                label = "Hesabına bağlı email adresini gir"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        AuthEditText(
            label = "Email",
            hint = "Email adresiniz",
            isPassword = false,
            value = state.email,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
            onImeAction = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },

            onValueChange = { newEmail ->
                viewModel.handleIntent(FPIntent.SetEmail(newEmail))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppBtn(text = "Bağlantı Gönder") {
            viewModel.handleIntent(FPIntent.Submit)
        }


    }
}