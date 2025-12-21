package com.expense.expensetracking.presentation.cards.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.presentation.cards.component.CardScreenItem
import com.expense.expensetracking.ui.theme.Manrope

@Composable
fun CardsScreen(
    viewModel: CardSharedViewModel = hiltViewModel(),
    onNavigateCardDetail: () -> Unit,
    onNavigateAddItem: () -> Unit
){
    val state by viewModel.uiDataState.collectAsState()

    when(state.uiState){
        is UiState.Idle -> {
            CardIdle(
                viewModel,
                state,
                onNavigateCardDetail
            ) {
                onNavigateAddItem()
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
fun CardIdle(
    viewModel: CardSharedViewModel,
    state: SharedCardState,
    onNavigateCardDetail: () -> Unit,
    onNavigateAddItem: () -> Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            CustomTopAppBar(
                icon = Icons.Rounded.Add,
                header = "Kartlarım",
                isBackBtnActive = false,
                isTrailingIconActive = true
            ){
                onNavigateAddItem()
            }
        }
        if(state.cardList.isNotEmpty()){
            items(state.cardList){ card ->
                CardScreenItem(card){
                    onNavigateCardDetail()
                }
            }
        }else{
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Herhangi bir kart bulunamadı",
                        modifier =Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontFamily = Manrope,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}