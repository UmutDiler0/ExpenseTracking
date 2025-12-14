package com.expense.expensetracking.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.expense.expensetracking.common.util.CardGraph
import com.expense.expensetracking.common.util.HomeGraph
import com.expense.expensetracking.common.util.ProfileGraph
import com.expense.expensetracking.common.util.ReportsGraph

sealed class BottomBarItems(
    val route: Any,
    val label: String,
    val icon: ImageVector
) {
    data object Home : BottomBarItems(
        route = HomeGraph,
        label = "Ana Sayfa",
        icon = Icons.Default.Home
    )

    data object Reports : BottomBarItems(
        route = ReportsGraph,
        label = "Raporlar",
        icon = Icons.Default.StackedBarChart
    )

    data object Profile : BottomBarItems(
        route = ProfileGraph,
        label = "Profil",
        icon = Icons.Default.Person
    )

    data object Cards : BottomBarItems(
        route = CardGraph,
        label = "Kartlar",
        icon = Icons.Default.CreditCard
    )
}


val bottomNavItems = listOf(
    BottomBarItems.Home,
    BottomBarItems.Reports,
    BottomBarItems.Cards,
    BottomBarItems.Profile,

)