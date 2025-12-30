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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expense.expensetracking.R
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.BackBtn
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.auth.component.AuthEditText
import com.expense.expensetracking.presentation.auth.component.AuthHeader
import com.expense.expensetracking.ui.theme.Dimens

@Composable
fun ForgotPasswordScreen(
    viewModel: FPViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit
){
    val state by viewModel.uiDataState.collectAsStateWithLifecycle()
    
    LaunchedEffect(state.uiState) {
        when(val uiState = state.uiState) {
            is UiState.Success -> {
                // Success message already handled by UI
            }
            else -> {}
        }
    }
    
    when(state.uiState){
        is UiState.Idle, is UiState.Error, is UiState.Success -> {
            ForgotPasswordIdleScreen(viewModel, state, onPopBackStack)
        }
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
            .padding(Dimens.paddingLarge)
            .imePadding()
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
                header = stringResource(R.string.forgot_password_title),
                label = stringResource(R.string.forgot_password_subtitle)
            )
        }

        Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))

        AuthEditText(
            label = stringResource(R.string.forgot_password_email_label),
            hint = stringResource(R.string.forgot_password_email_hint),
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

        Spacer(modifier = Modifier.height(Dimens.spacingLarge))

        // Error message
        if (state.uiState is UiState.Error) {
            Text(
                text = (state.uiState as UiState.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(bottom = Dimens.paddingSmall)
            )
        }

        // Success message
        if (state.uiState is UiState.Success) {
            Text(
                text = stringResource(R.string.forgot_password_success),
                color = Color.Green,
                modifier = Modifier.padding(bottom = Dimens.paddingSmall)
            )
        }

        AppBtn(text = stringResource(R.string.forgot_password_button)) {
            viewModel.handleIntent(FPIntent.Submit)
        }
    }
}