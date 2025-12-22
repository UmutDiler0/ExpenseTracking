package com.expense.expensetracking.presentation.home.add_balance

import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.expense.expensetracking.ui.theme.SurfaceDark
import com.expense.expensetracking.ui.theme.SurfaceLight
import com.expense.expensetracking.ui.theme.TextDark
import com.expense.expensetracking.ui.theme.TextGrayDark
import com.expense.expensetracking.ui.theme.TextGrayLight
import com.expense.expensetracking.ui.theme.TextLight

@Composable
fun AddBalanceScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit
){
    val state by viewModel.uiDataState.collectAsState()

    LaunchedEffect(state.uiState) {
        if(state.uiState == UiState.Success){
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
){
    val contentColor = if (isSystemInDarkTheme()) TextLight else TextDark
    val labelColor = if (isSystemInDarkTheme()) TextGrayDark else TextGrayLight
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        CustomTopAppBar(
            icon = Icons.Default.CreditCard,
            header = "Gelir Ekle",
            isBackBtnActive = true,
            isTrailingIconActive = false
        ) {
            onPopBackStack()
        }
        CustomInputField(
            label = "Miktar",
            value = state.addBalance,
            onValueChange = {
                viewModel.handleIntent(HomeIntent.AddBalanceValue(it))
            },
            placeholder = "0,00",
            isNumeric = true,
            isSingleLine = true,
            backgroundColor = if (isSystemInDarkTheme()) SurfaceDark else SurfaceLight,
            textColor = contentColor,
            labelColor = labelColor
        )
        Column {
            Text(
                text = "Kart Seçimi",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = labelColor
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SelectionRow(
                icon = Icons.Default.CreditCard,
                title = state.selectedCardItem.name,
                backgroundColor = if (isSystemInDarkTheme()) SurfaceDark else SurfaceLight,
                contentColor = contentColor,
                iconBgColor = if (isSystemInDarkTheme()) Color.White.copy(0.1f) else Color(
                    0xFFD1D5DB
                )
            ){
                if(state.user.cardList.isNotEmpty()){
                    viewModel.handleIntent(HomeIntent.SetMenu(Menu.CARDS))
                }else{
                    Toast.makeText(context ,"Herhngi bir kart bulunamadı önce kart ekleyiniz", Toast.LENGTH_LONG).show()
                }
            }
        }
        AppBtn(
            "Kaydet"
        ){
            viewModel.addBalance()
        }
    }
}