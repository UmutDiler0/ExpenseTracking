package com.expense.expensetracking.presentation.cards.addcard

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCard
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.expense.expensetracking.R
import com.expense.expensetracking.common.component.AddCardTextField
import com.expense.expensetracking.common.component.AppBtn
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.cards.ui.CardSharedViewModel
import com.expense.expensetracking.presentation.cards.ui.SharedCardIntent
import com.expense.expensetracking.presentation.cards.ui.SharedCardState
import com.expense.expensetracking.ui.theme.BackgroundDark
import com.expense.expensetracking.ui.theme.BackgroundLight

@Composable
fun AddCardScreen(
    viewModel: CardSharedViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit
){
    val state by viewModel.uiDataState.collectAsState()

    when(state.uiState){
        is UiState.Idle -> {
            // Success'den Idle'a döndükten sonra geri git
            if (state.addCardName.isEmpty() && state.addCardBalance.isEmpty() && state.cardNameError == null) {
                // Bu durum Success'den sonra state temizlendiğinde oluşur
                // Ama ilk açılışta da bu durum olur, o yüzden dikkatli olmalıyız
            }
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
) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 24.dp), // Butonun en altta sıkışmaması için
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(
            icon = Icons.Rounded.AddCard, // Daha bağlam odaklı bir ikon
            header = stringResource(R.string.add_card_title),
            isBackBtnActive = true,
            isTrailingIconActive = false
        ) {
            onPopBackStack()
        }

        // Giriş Alanları
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                AddCardTextField(
                    header = stringResource(R.string.add_card_name_label),
                    value = state.addCardName,
                    hint = stringResource(R.string.add_card_name_hint),
                    isError = state.cardNameError != null,
                    onTextValueChange = {
                        if (it.length <= 24) {
                            viewModel.handleIntent(SharedCardIntent.SetCardName(it))
                        } else {
                            Toast.makeText(context, context.getString(R.string.add_card_name_max_length), Toast.LENGTH_SHORT).show()
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    headerColor = MaterialTheme.colorScheme.onSurface
                )
                
                AnimatedVisibility(
                    visible = state.cardNameError != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        text = state.cardNameError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 4.dp)
                            .fillMaxWidth()
                    )
                }
            }

            AddCardTextField(
                header = stringResource(R.string.add_card_balance_label),
                value = state.addCardBalance,
                hint = stringResource(R.string.add_card_balance_hint),
                onTextValueChange = {
                    viewModel.handleIntent(SharedCardIntent.SetCardBalance(it))
                },
                backgroundColor = MaterialTheme.colorScheme.surface,
                headerColor = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Kaydet butonunu ekranın en altına iter

        AppBtn(
            text = stringResource(R.string.add_card_save_button),
        ) {
            when {
                state.addCardName.isBlank() -> {
                    Toast.makeText(context, context.getString(R.string.add_card_name_empty), Toast.LENGTH_SHORT).show()
                }
                state.addCardBalance.isEmpty() -> {
                    Toast.makeText(context, context.getString(R.string.add_card_balance_empty), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewModel.addCardToDB()
                }
            }
        }
            }
        }

