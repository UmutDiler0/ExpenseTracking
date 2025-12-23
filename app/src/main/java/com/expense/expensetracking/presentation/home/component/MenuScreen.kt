package com.expense.expensetracking.presentation.home.component

import android.view.MenuItem
import androidx.benchmark.traceprocessor.Insight
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.domain.model.Category
import com.expense.expensetracking.presentation.home.ui.HomeIntent
import com.expense.expensetracking.presentation.home.ui.HomeState
import com.expense.expensetracking.presentation.home.ui.HomeViewModel
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.TextBlack

@Composable
fun MenuScreen(
    state: HomeState,
    viewModel: HomeViewModel,
    menuType: Menu
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Text(
                if(menuType == Menu.CATEGORY) "Kategori Seçiniz" else "Kart Seçiniz",
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                textAlign = TextAlign.Center,
                fontFamily = Manrope,
                fontWeight = FontWeight.Bold,
            )
        }
        if (menuType == Menu.CATEGORY) {
            itemsIndexed(Category.categoryList){ index, item ->
                MenuItem(item.categoryName, item.icon){
                    viewModel.handleIntent(HomeIntent.SelectCategory(Category.categoryList.get(index)))
                    viewModel.handleIntent(HomeIntent.SetMenu(Menu.IDLE))
                }
            }
        } else {
            itemsIndexed(state.user.cardList){ index, item ->
                MenuItem(
                    item.name,
                    Icons.Filled.CreditCard
                ){
                    viewModel.handleIntent(HomeIntent.SelectCard(state.user.cardList.get(index)))
                    viewModel.handleIntent(HomeIntent.SetMenu(Menu.IDLE))
                }
            }
        }
    }

}

@Composable
fun MenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable{
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = TextBlack,
                        shape = CircleShape
                    )
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = Color.White
                )
            }
            Text(
                title,
                color = Color.White,
                fontFamily = Manrope,
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Color.White, modifier = Modifier.fillMaxWidth())
    }
}