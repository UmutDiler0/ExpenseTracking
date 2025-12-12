package com.expense.expensetracking.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppIcon(){
    Box(
        modifier = Modifier
            .size(96.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF4ADE80), // green-400
                        Color(0xFF16A34A)  // green-600
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .shadow(
                elevation = 10.dp,
                spotColor = Color(0xFF22C55E).copy(alpha = 0.2f),
                shape = RoundedCornerShape(22.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Wallet,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )
    }
}