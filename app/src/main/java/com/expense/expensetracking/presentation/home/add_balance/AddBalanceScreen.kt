package com.expense.expensetracking.presentation.home.add_balance

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.R
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.home.component.CustomInputField
import com.expense.expensetracking.presentation.home.component.Menu
import com.expense.expensetracking.presentation.home.component.MenuScreen
import com.expense.expensetracking.presentation.home.component.SelectionRow
import com.expense.expensetracking.presentation.home.ui.HomeIntent
import com.expense.expensetracking.presentation.home.ui.HomeState
import com.expense.expensetracking.presentation.home.ui.HomeViewModel
import com.expense.expensetracking.ui.theme.PrimaryGreen

@Composable
fun AddBalanceScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit
){
    val state by viewModel.uiDataState.collectAsState()

    LaunchedEffect(state.uiState) {
        if(state.uiState == UiState.Success){
            viewModel.resetUiState()
            onPopBackStack()
        }
    }

    when(state.uiState){
        is UiState.Idle -> {
            if(state.currentMenuState == Menu.IDLE){
                AddBalanceIdle(
                    viewModel,
                    state
                ) {
                    onPopBackStack()
                }
            }else{
                MenuScreen(
                    state,
                    viewModel,
                    menuType = Menu.CARDS
                )
            }

        }
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Error -> {}
        is UiState.Success -> {}
    }
}

@Composable
fun AddBalanceIdle(
    viewModel: HomeViewModel,
    state: HomeState,
    onPopBackStack: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 24.dp), // Buton için alt boşluk
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        CustomTopAppBar(
            icon = Icons.Rounded.AccountBalanceWallet,
            header = stringResource(R.string.add_balance_title),
            isBackBtnActive = true,
            isTrailingIconActive = false
        ) { onPopBackStack() }

        // Miktar Girişi
        CustomInputField(
            label = stringResource(R.string.add_balance_amount_label),
            value = state.addBalance,
            onValueChange = { newValue ->
                val numericValue = newValue.toLongOrNull() ?: 0
                when {
                    numericValue < 1 && newValue.isNotEmpty() -> {
                        Toast.makeText(context, context.getString(R.string.add_balance_min_amount), Toast.LENGTH_SHORT).show()
                    }
                    numericValue > 999999999 -> {
                        Toast.makeText(context, context.getString(R.string.add_balance_max_amount), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.handleIntent(HomeIntent.AddBalanceValue(newValue))
                    }
                }
            },
            placeholder = stringResource(R.string.add_balance_amount_hint),
            isNumeric = true,
            backgroundColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            isSingleLine = true,
            minValue = 1,
            maxValue = 999999999
        )

        // Kart Seçim Alanı
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
                text = stringResource(R.string.add_balance_card_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            SelectionRow(
                icon = Icons.Rounded.CreditCard,
                title = if (state.selectedCardItem.name.isEmpty()) stringResource(R.string.add_balance_select_card) else state.selectedCardItem.name,
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = if (state.selectedCardItem.name.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface,
                iconBgColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                if (state.user.cardList.isNotEmpty()) {
                    viewModel.handleIntent(HomeIntent.SetMenu(Menu.CARDS))
                } else {
                    Toast.makeText(context, context.getString(R.string.add_balance_add_card_first), Toast.LENGTH_SHORT).show()
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Butonu en alta itmek için

        AppBtn(
            text = stringResource(R.string.add_balance_save_button),
        ) {
            when {
                state.addBalance.isEmpty() -> {
                    Toast.makeText(context, context.getString(R.string.add_balance_enter_amount), Toast.LENGTH_SHORT).show()
                }
                state.addBalance.toLongOrNull() == null || state.addBalance.toLong() < 1 -> {
                    Toast.makeText(context, context.getString(R.string.add_balance_min_amount), Toast.LENGTH_SHORT).show()
                }
                state.selectedCardItem.name.isEmpty() -> {
                    Toast.makeText(context, context.getString(R.string.add_balance_select_card_error), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewModel.addBalance()
                }
            }
        }
    }
}