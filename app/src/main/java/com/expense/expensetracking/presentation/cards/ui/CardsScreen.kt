package com.expense.expensetracking.presentation.cards.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.presentation.cards.component.CardScreenItem

@Composable
fun CardsScreen(
    viewModel: CardSharedViewModel = hiltViewModel(),
    onNavigateCardDetail: () -> Unit,
    onNavigateAddItem: () -> Unit
){
    val state by viewModel.uiDataState.collectAsState()

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
        items(CardItem.tempList){ card ->
            CardScreenItem(card){
                onNavigateCardDetail()
            }
        }
    }
}