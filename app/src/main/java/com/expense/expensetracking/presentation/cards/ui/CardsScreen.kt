package com.expense.expensetracking.presentation.cards.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracking.R
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.ErrorScreen
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.presentation.cards.card_detail.CardDetailScreen
import com.expense.expensetracking.presentation.cards.component.CardScreenItem
import com.expense.expensetracking.ui.theme.BackgroundDark
import com.expense.expensetracking.ui.theme.BackgroundLight
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.TextBlack
import com.expense.expensetracking.ui.theme.TextGrayDark
import com.expense.expensetracking.ui.theme.TextGrayLight
import com.expense.expensetracking.ui.theme.TextWhite

@Composable
fun CardsScreen(
    viewModel: CardSharedViewModel = hiltViewModel(),
    onNavigateAddItem: () -> Unit
){
    val state by viewModel.uiDataState.collectAsState()

    when(state.uiState){
        is UiState.Idle -> {
            if(state.currentCardStage == CardStage.IDLE){
                CardIdle(
                    viewModel,
                    state,
                ) {
                    onNavigateAddItem()
                }
            }else{
                CardDetailScreen(
                    viewModel,
                    state
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardIdle(
    viewModel: CardSharedViewModel,
    state: SharedCardState,
    onNavigateAddItem: () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = if (state.cardList.isEmpty()) Arrangement.Center else Arrangement.spacedBy(12.dp)
    ) {
        // Liste doluysa TopBar'ı göster
        if (state.cardList.isNotEmpty()) {
            item {
                CustomTopAppBar(
                    icon = Icons.Rounded.Add,
                    header = stringResource(R.string.cards_title),
                    isBackBtnActive = false,
                    isTrailingIconActive = true
                ) {
                    onNavigateAddItem()
                }
            }

            itemsIndexed(
                items = state.cardList,
                key = { _, card -> card.name } // Listede kaymalar olmaması için 'key' kullanımı şarttır
            ) { index, card ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { dismissValue ->
                        if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                            viewModel.deleteCard(card)
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false, // Sadece sağdan sola (silme)
                    modifier = Modifier.padding(vertical = 4.dp), // Kartlar arası mesafe
                    backgroundContent = {
                        val isDismissing = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
                        val backgroundColor = if (isDismissing) Color(0xFFEF4444) else Color.Transparent

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp) // Kartın horizontal padding'i ile aynı olmalı
                                .clip(RoundedCornerShape(20.dp)) // Kartın yuvarlaklığı ile uyumlu
                                .background(backgroundColor)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            // Sadece kullanıcı kaydırdığında içeriği gösteriyoruz
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = stringResource(R.string.cards_delete),
                                    color = Color.White,
                                    fontFamily = Manrope,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = "Sil",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    },
                    content = {
                        // Mevcut Kart Bileşenin
                        CardScreenItem(card) {
                            viewModel.handleIntent(SharedCardIntent.SetCurrentCard(card))
                            viewModel.handleIntent(SharedCardIntent.SetCurrentCardStage(CardStage.EDIT))
                        }
                    }
                )
            }
        } else {
            // BOŞ DURUM (CTA) EKRANI
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxSize() // Ekranın tamamını kaplaması için
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Payments,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = PrimaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(R.string.cards_empty_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = Manrope,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.cards_empty_description),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    AppBtn(
                        text = stringResource(R.string.cards_add_button),
                    ) {
                        onNavigateAddItem()
                    }
                }
            }
        }
    }
}