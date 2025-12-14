package com.expense.expensetracking.domain.model

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.ui.graphics.vector.ImageVector

data class ExpenseItem(
    val icon: ImageVector = Icons.Rounded.Image,
    val title: String = "",
    val desc: String  ="",
    val price: Int = 450,
    val isPriceUp: Boolean = false
    )