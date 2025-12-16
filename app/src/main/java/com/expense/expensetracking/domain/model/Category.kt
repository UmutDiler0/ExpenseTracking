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

data class Category(
    val categoryName: String,
    val icon: ImageVector
){
    companion object {
        val categoryList = listOf(
            Category(
                categoryName = "Market",
                icon = Icons.Default.ShoppingCart
            ),
            Category(
                categoryName = "Yemek",
                icon = Icons.Default.Restaurant
            ),
            Category(
                categoryName = "Ulaşım",
                icon = Icons.Default.DirectionsBus
            ),
            Category(
                categoryName = "Kahve",
                icon = Icons.Default.LocalCafe
            ),
            Category(
                categoryName = "Eğlence",
                icon = Icons.Default.Movie
            ),
            Category(
                categoryName = "Sağlık",
                icon = Icons.Default.LocalHospital
            ),
            Category(
                categoryName = "Faturalar",
                icon = Icons.Default.Receipt
            ),
            Category(
                categoryName = "Giyim",
                icon = Icons.Default.Checkroom
            )
        )
    }
}