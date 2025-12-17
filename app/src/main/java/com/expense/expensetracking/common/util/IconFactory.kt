package com.expense.expensetracking.common.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector

object IconFactory {

    fun getIcon(iconName: String): ImageVector {
        return when (iconName) {
            "food" -> Icons.Rounded.Fastfood
            "transport" -> Icons.Rounded.DirectionsCar
            "shopping" -> Icons.Rounded.ShoppingBag
            "health" -> Icons.Rounded.MedicalServices
            "bills" -> Icons.Rounded.Receipt
            "entertainment" -> Icons.Rounded.Movie
            "salary" -> Icons.Rounded.AttachMoney
            "investment" -> Icons.Rounded.TrendingUp
            else -> Icons.Rounded.Category
        }
    }
}