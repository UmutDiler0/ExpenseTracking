
package com.expense.expensetracking.presentation.cards.card_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Nfc
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.cards.ui.CardSharedViewModel
import com.expense.expensetracking.presentation.cards.ui.CardStage
import com.expense.expensetracking.presentation.cards.ui.SharedCardIntent
import com.expense.expensetracking.presentation.cards.ui.SharedCardState
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.PrimaryGreenDark
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
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(
            icon = Icons.Rounded.CreditCard,
            header = "Kart Detayı",
            isBackBtnActive = true,
            isTrailingIconActive = false
        ) {
            viewModel.handleIntent(SharedCardIntent.SetCurrentCardStage(CardStage.IDLE))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- GÖRSEL BANKA KARTI (UX Vurgusu) ---
        VisualBankCard(
            name = state.currentCard.name,
            balance = state.currentCard.balance.toString()
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- BİLGİ VE DÜZENLEME ALANI ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Kart Bilgileri",
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.titleMedium,
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            CardInfoItem(
                header = "Kart İsmi",
                value = state.currentCard.name,
                icon = Icons.Rounded.Badge
            ) {
                // Kart ismi düzenleme tetiği buraya gelebilir
            }

            CardInfoItem(
                header = "Bakiye",
                value = "${state.currentCard.balance} ₺",
                icon = Icons.Rounded.AccountBalanceWallet
            ) {
                // Bakiye düzenleme veya yükleme tetiği
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- SİLME BUTONU (UX: Tehlikeli işlemler genellikle en altta ve kırmızı olur) ---
        TextButton(
            onClick = { /* Silme Intent'i */ },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                "Bu Kartı Sil",
                color = Color(0xFFFF5252),
                fontFamily = Manrope,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun VisualBankCard(name: String, balance: String) {
    val isDark = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            // Sağ Üst İkon
            Icon(
                Icons.Rounded.Nfc,
                null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.TopEnd)
            )

            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = balance + " ₺",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryGreen,
                    fontFamily = Manrope
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = name.uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    letterSpacing = 2.sp,
                    fontFamily = Manrope
                )
            }
        }
    }
}

@Composable
fun CardInfoItem(
    header: String,
    value: String,
    icon: ImageVector,
    onEditClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onEditClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isDark) Color.White.copy(0.05f) else Color(0xFFF3F4F6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = if (isDark) PrimaryGreen else PrimaryGreenDark, modifier = Modifier.size(20.dp))
        }

        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(header, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }

        Icon(
            Icons.Rounded.Edit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.size(18.dp)
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