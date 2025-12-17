package com.expense.expensetracking.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark

@Composable
fun LoadingScreen(

){
    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                color = if(isSystemInDarkTheme()) SurfaceDark.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.4f)
            ),
        contentAlignment = Alignment.Center
    ){
        AppIcon()
        CircularProgressIndicator(
            modifier = Modifier.size(200.dp),
            color = PrimaryGreen
        )
    }
}
