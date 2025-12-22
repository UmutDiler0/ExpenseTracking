package com.expense.expensetracking.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.expense.expensetracking.common.util.IconFactory
import com.google.firebase.Timestamp

data class ExpenseItem(
    val iconName: String = "others",
    val title: String = "",
    val desc: String = "",
    val price: Int = 450,
    val priceUp: Boolean = false,
    val spendOrAddCard: CardItem = CardItem(),
    val spendDate: Timestamp = Timestamp.now()
) {
    val icon: ImageVector
        get() = IconFactory.getIcon(iconName)
}