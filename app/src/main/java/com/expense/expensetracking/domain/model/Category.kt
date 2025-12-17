package com.expense.expensetracking.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.expense.expensetracking.common.util.IconFactory

data class Category(
    val categoryName: String,
    val iconName: String
){
    val icon: ImageVector
        get() = IconFactory.getIcon(iconName)

    companion object {
        val categoryList = listOf(
            Category(
                categoryName = "Market",
                iconName = "shopping"
            ),
            Category(
                categoryName = "Yemek",
                iconName = "food"
            ),
            Category(
                categoryName = "Ulaşım",
                iconName = "transport"
            ),
            Category(
                categoryName = "Kahve",
                iconName = "coffee"
            ),
            Category(
                categoryName = "Eğlence",
                iconName = "entertainment"
            ),
            Category(
                categoryName = "Sağlık",
                iconName = "health"
            ),
            Category(
                categoryName = "Faturalar",
                iconName = "bills"
            ),
            Category(
                categoryName = "Giyim",
                iconName = "clothing"
            )
        )
    }
}