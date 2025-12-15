
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.presentation.cards.ui.CardSharedViewModel
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark

@Composable
fun CardDetailScreen(
    viewModel: CardSharedViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit,
){
    val state by viewModel.uiDataState.collectAsState()
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
        ){}
        Icon(
            imageVector = Icons.Rounded.CreditCard,
            contentDescription = "",
            tint = PrimaryGreen,
            modifier = Modifier.size(100.dp)
        )
        CardInfo()
        CardInfo()
    }
}

@Composable
fun CardInfo(

){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Kart İsmi",
                fontFamily = Manrope,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "250",
            )
        }
    }
}