package com.expense.expensetracking.presentation.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.R
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.ErrorScreen
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.home.component.AddOrDecBalanceBtn
import com.expense.expensetracking.presentation.home.component.BalanceCard
import com.expense.expensetracking.presentation.home.component.ExpensesList
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.PrimaryGreenDark
import com.expense.expensetracking.ui.theme.TextBlack
import com.expense.expensetracking.ui.theme.TextWhite

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
        is UiState.Error -> {
            ErrorScreen()
        }
        is UiState.Success -> {}
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
            header = stringResource(R.string.home_title),
            isBackBtnActive = false,
            isTrailingIconActive = true
        ){}
        
        // Kart Filtre Dropdown
        CardFilterDropdown(
            selectedCard = state.selectedCardFilter,
            cards = state.user.cardList.map { it.name },
            onCardSelected = { cardName ->
                viewModel.handleIntent(HomeIntent.FilterByCard(cardName))
            }
        )
        
        // Seçili kart varsa o kartın bilgilerini, yoksa toplam bakiyeyi göster
        val selectedCardData = state.user.cardList.find { it.name == state.selectedCardFilter }
        BalanceCard(
            value = selectedCardData?.balance ?: state.user.totalBalance,
            cardName = selectedCardData?.name
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Butonlar arası sabit boşluk
        ) {
            // Gider Butonu (Ağırlık: 1)
            AddOrDecBalanceBtn(
                isSpend = true,
                modifier = Modifier.weight(1f),
                onClick = onNavigateSpendBalanceScreen
            )

            // Gelir Butonu (Ağırlık: 1)
            AddOrDecBalanceBtn(
                isSpend = false,
                modifier = Modifier.weight(1f),
                onClick = onNavigateAddBalanceScreen
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        if(state.filteredExpenses.isEmpty()){
            EmptyExpensesState(
                onAddExpenseClick = onNavigateSpendBalanceScreen
            )
        }else{
            ExpensesList(
                state.filteredExpenses
            )
        }

    }
}

@Composable
fun CardFilterDropdown(
    selectedCard: String?,
    cards: List<String>,
    onCardSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = selectedCard ?: stringResource(R.string.home_filter_all)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayText,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            // Tümü seçeneği
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.home_filter_all),
                        fontFamily = Manrope,
                        color = if (selectedCard == null) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        fontWeight = if (selectedCard == null) FontWeight.Bold else FontWeight.Normal
                    )
                },
                onClick = {
                    onCardSelected(null)
                    expanded = false
                }
            )
            
            // Kart seçenekleri
            cards.forEach { cardName ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = cardName,
                            fontFamily = Manrope,
                            color = if (cardName == selectedCard) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            fontWeight = if (cardName == selectedCard) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onCardSelected(cardName)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyExpensesState(
    onAddExpenseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
    ) {
        // İkon
        Icon(
            imageVector = Icons.Outlined.Receipt,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )

        // Başlık
        Text(
            text = stringResource(R.string.home_empty_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Manrope,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        // Açıklama
        Text(
            text = stringResource(R.string.home_empty_description),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Manrope,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        

        
        // CTA Butonu
        Button(
            onClick = onAddExpenseClick,
            modifier = Modifier
                .fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.home_add_expense),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Manrope
            )
        }
    }
}