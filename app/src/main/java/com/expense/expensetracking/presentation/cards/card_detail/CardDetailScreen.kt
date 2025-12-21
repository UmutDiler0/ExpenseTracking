
package com.expense.expensetracking.presentation.cards.card_detail

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.presentation.cards.ui.CardSharedViewModel
import com.expense.expensetracking.presentation.cards.ui.CardStage
import com.expense.expensetracking.presentation.cards.ui.SharedCardIntent
import com.expense.expensetracking.presentation.cards.ui.SharedCardState
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark

@Composable
fun CardDetailScreen(
    viewModel: CardSharedViewModel,
    state: SharedCardState,
){


    when(state.uiState){
        is UiState.Success -> {}
        is UiState.Loading -> {
            LoadingScreen()
        }
        is UiState.Error -> {}
        is UiState.Idle -> {
            CardDetailIdleScreen(
                viewModel,
                state,
            )
        }
    }


}

@Composable
fun CardDetailIdleScreen(
    viewModel: CardSharedViewModel,
    state: SharedCardState,
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        CustomTopAppBar(
            icon = Icons.Filled.Image,
            header = "Kart Detayı",
            isBackBtnActive = true,
            isTrailingIconActive = false
        ){
            viewModel.handleIntent(
                SharedCardIntent.SetCurrentCardStage(CardStage.IDLE)
            )
        }
        Icon(
            imageVector = Icons.Rounded.CreditCard,
            contentDescription = "",
            tint = PrimaryGreen,
            modifier = Modifier.size(100.dp)
        )
        CardInfo(
            "Kart İsmi",
            state.currentCard.name
        )
        CardInfo(
            "Bakiye",
            state.currentCard.balance.toString()
        )
    }
}

@Composable
fun CardInfo(
    header: String,
    cardValues: String
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            header,
            fontSize = 16.sp,
            fontFamily = Manrope,
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceDark
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    cardValues,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "",
                    tint = Color.White,
                )
            }
        }
    }
}