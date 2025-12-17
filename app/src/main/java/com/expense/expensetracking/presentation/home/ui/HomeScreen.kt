package com.expense.expensetracking.presentation.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.home.component.AddOrDecBalanceBtn
import com.expense.expensetracking.presentation.home.component.BalanceCard
import com.expense.expensetracking.presentation.home.component.ExpensesList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
 onNavigateAddBalanceScreen: () -> Unit,
 onNavigateSpendBalanceScreen: () -> Unit
){
    val state by viewModel.uiDataState.collectAsState()

    when(state.uiState){
        is UiState.Idle -> {
            HomeIdle(
                viewModel,
                state,
                onNavigateAddBalanceScreen
            ) {
                onNavigateSpendBalanceScreen()
            }
        }
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Error -> {}
        is UiState.Success<*> -> {}
    }

}

@Composable
fun HomeIdle(
    viewModel: HomeViewModel,
    state: HomeState,
    onNavigateAddBalanceScreen: () -> Unit,
    onNavigateSpendBalanceScreen: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        CustomTopAppBar(
            icon = Icons.Rounded.Notifications,
            header = "Ana Ekran",
            isBackBtnActive = false,
            isTrailingIconActive = true
        ){}
        BalanceCard()
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AddOrDecBalanceBtn(true) {
                onNavigateSpendBalanceScreen()

            }
            AddOrDecBalanceBtn(false) {
                onNavigateAddBalanceScreen()
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        ExpensesList()
    }
}