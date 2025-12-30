package com.expense.expensetracking.presentation.home.spend_balance

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.expense.expensetracking.ui.theme.BackgroundDark
import com.expense.expensetracking.ui.theme.BackgroundLight
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark
import com.expense.expensetracking.ui.theme.TextBlack
import com.expense.expensetracking.ui.theme.TextGrayDark
import com.expense.expensetracking.ui.theme.TextGrayLight
import com.expense.expensetracking.ui.theme.TextWhite

@Composable
fun SpendBalanceScreen(
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
            if(state.currentMenuState != Menu.IDLE){
                MenuScreen(
                    state = state,
                    viewModel = viewModel,
                    menuType = state.currentMenuState
                )
            }else{
                SpendBalanceIdle(
                    viewModel,
                    state
                ) {
                    onPopBackStack()
                }
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
fun SpendBalanceIdle(
    viewModel: HomeViewModel,
    state: HomeState,
    onPopBackStack: () -> Unit
) {
    val context = LocalContext.current
    var showInsufficientBalanceDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- Üst Bar ---
        CustomTopAppBar(
            icon = Icons.Rounded.Payments, // Gider için daha uygun bir ikon
            header = stringResource(R.string.spend_balance_title),
            isBackBtnActive = true,
            isTrailingIconActive = false
        ) {
            onPopBackStack()
        }

        // --- Tutar Girişi ---
        CustomInputField(
            label = stringResource(R.string.spend_balance_amount_label),
            value = state.spendBalance,
            onValueChange = { newValue ->
                val numericValue = newValue.toLongOrNull() ?: 0
                when {
                    numericValue < 1 && newValue.isNotEmpty() -> {
                        Toast.makeText(context, context.getString(R.string.spend_balance_min_amount), Toast.LENGTH_SHORT).show()
                    }
                    numericValue > 999999999 -> {
                        Toast.makeText(context, context.getString(R.string.spend_balance_max_amount), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.handleIntent(HomeIntent.AddSpendValue(newValue))
                    }
                }
            },
            placeholder = stringResource(R.string.spend_balance_amount_hint),
            isNumeric = true,
            backgroundColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            isSingleLine = true,
            minValue = 1,
            maxValue = 999999999
        )

        // --- Seçim Alanları ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Kategori Bölümü
            Column {
                Text(
                    text = stringResource(R.string.spend_balance_category_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                SelectionRow(
                    icon = if (state.selectedCategory.categoryName.isEmpty())
                        Icons.Rounded.Category else state.selectedCategory.icon,
                    title = state.selectedCategory.categoryName.ifEmpty { stringResource(R.string.spend_balance_select_category) },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = if (state.selectedCategory.categoryName.isEmpty()) 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else 
                        MaterialTheme.colorScheme.onSurface,
                    iconBgColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    viewModel.handleIntent(HomeIntent.SetMenu(Menu.CATEGORY))
                }
            }

            // Kart Bölümü
            Column {
                Text(
                    text = stringResource(R.string.spend_balance_card_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                SelectionRow(
                    icon = Icons.Rounded.CreditCard,
                    title = state.selectedCardItem.name.ifEmpty { stringResource(R.string.spend_balance_select_card) },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = if (state.selectedCardItem.name.isEmpty()) 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else 
                        MaterialTheme.colorScheme.onSurface,
                    iconBgColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    if (state.user.cardList.isNotEmpty()) {
                        viewModel.handleIntent(HomeIntent.SetMenu(Menu.CARDS))
                    } else {
                        Toast.makeText(context, context.getString(R.string.spend_balance_add_card_first), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Aksiyon Butonu ---
        AppBtn(
            text = stringResource(R.string.spend_balance_save_button),

        ) {
            // Basit Validation
            when {
                state.spendBalance.isEmpty() -> {
                    Toast.makeText(context, context.getString(R.string.spend_balance_enter_amount), Toast.LENGTH_SHORT).show()
                }
                state.spendBalance.toLongOrNull() == null || state.spendBalance.toLong() < 1 -> {
                    Toast.makeText(context, context.getString(R.string.spend_balance_min_amount), Toast.LENGTH_SHORT).show()
                }
                state.selectedCategory.categoryName.isEmpty() -> {
                    Toast.makeText(context, context.getString(R.string.spend_balance_select_category_error), Toast.LENGTH_SHORT).show()
                }
                state.selectedCardItem.name.isEmpty() -> {
                    Toast.makeText(context, context.getString(R.string.spend_balance_select_card_error), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val spendAmount = state.spendBalance.toLong()
                    val cardBalance = state.selectedCardItem.balance
                    
                    // Bakiye kontrolü
                    if (spendAmount > cardBalance) {
                        showInsufficientBalanceDialog = true
                    } else {
                        viewModel.addSpend()
                    }
                }
            }
        }
    }
    
    // Yetersiz bakiye dialog'u
    if (showInsufficientBalanceDialog) {
        AlertDialog(
            onDismissRequest = { showInsufficientBalanceDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.spend_balance_insufficient_balance_title),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.spend_balance_insufficient_balance_message,
                        state.spendBalance,
                        state.selectedCardItem.balance.toString()
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showInsufficientBalanceDialog = false
                        viewModel.addSpend()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444),
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.spend_balance_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showInsufficientBalanceDialog = false }
                ) {
                    Text(stringResource(R.string.spend_balance_cancel_dialog))
                }
            }
        )
    }
}
