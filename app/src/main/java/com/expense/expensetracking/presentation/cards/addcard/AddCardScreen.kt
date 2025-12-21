package com.expense.expensetracking.presentation.cards.addcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracking.common.component.AddCardTextField
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.cards.ui.CardSharedViewModel
import com.expense.expensetracking.presentation.cards.ui.SharedCardIntent
import com.expense.expensetracking.presentation.cards.ui.SharedCardState

@Composable
fun AddCardScreen(
    viewModel: CardSharedViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit
){
    val state by viewModel.uiDataState.collectAsState()

    when(state.uiState){
        is UiState.Idle -> {
            AddCardScreenIdle(
                viewModel,
                state
            ) {
                onPopBackStack()
            }
        }
        is UiState.Success -> {
            onPopBackStack()
        }
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Error -> {}
    }
}

@Composable
fun AddCardScreenIdle(
    viewModel: CardSharedViewModel,
    state: SharedCardState,
    onPopBackStack: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(
            icon = Icons.Rounded.Notifications,
            header = "Kart Ekle",
            isBackBtnActive = true,
            isTrailingIconActive = false
        ) {
            onPopBackStack()
        }
        AddCardTextField(
            header = "Kart Adı",
            value = state.addCardName,
            hint = "Kart Adını Giriniz"
        ) {
            viewModel.handleIntent(SharedCardIntent.SetCardName(it))
        }
        AddCardTextField(
            header = "Bakiye",
            value = state.addCardBalance,
            hint = "Bakiyenizi Giriniz"
        ) {
            viewModel.handleIntent(SharedCardIntent.SetCardBalance(it))
        }
        Spacer(modifier = Modifier.height(24.dp))
        AppBtn(
            "Kaydet"
        ) {
            viewModel.addCardToDB()
            onPopBackStack()
        }
    }
}