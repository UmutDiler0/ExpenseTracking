package com.expense.expensetracking.common.component

import android.graphics.drawable.Icon
import android.media.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.ui.theme.Manrope

@Composable
fun CustomTopAppBar(
    icon: ImageVector,
    header: String,
    isBackBtnActive: Boolean,
    isTrailingIconActive: Boolean,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if(isBackBtnActive){
            Icon(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                "",
                modifier = Modifier.size(32.dp).clickable{
                    onClick()
                }
            )
        }else{
            Box(modifier = Modifier.size(32.dp))
        }
        Text(
            header,
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp
        )
        if(isTrailingIconActive){
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier.size(32.dp).clickable{
                    onClick()
                }
            )
        }else{
            Box(modifier = Modifier.size(32.dp))
        }

    }
}