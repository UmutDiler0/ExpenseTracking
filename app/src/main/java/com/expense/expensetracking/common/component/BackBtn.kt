package com.expense.expensetracking.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.expensetracking.ui.theme.PrimaryGreen

@Composable
fun BackBtn(
    onClick: () -> Unit
){
    Icon(
        imageVector = Icons.Rounded.ArrowBackIosNew,
        contentDescription = "",
        tint = PrimaryGreen,
        modifier = Modifier.size(32.dp).clickable{
            onClick()
        }
    )
}