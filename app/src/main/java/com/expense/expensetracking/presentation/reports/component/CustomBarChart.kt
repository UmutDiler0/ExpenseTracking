package com.expense.expensetracking.presentation.reports.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.PrimaryGreenDark
import com.expense.expensetracking.ui.theme.TextGrayDark
import com.expense.expensetracking.ui.theme.TextGrayLight
import java.util.Calendar

@Composable
fun CustomBarChart(
    modifier: Modifier = Modifier,
    weeklyData: List<Float>
) {
    val isDark = isSystemInDarkTheme()
    val labels = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
    val maxVal = weeklyData.maxOrNull()?.takeIf { it > 0 } ?: 1f

    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val todayIndex = when (dayOfWeek) {
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        Calendar.SUNDAY -> 6
        else -> -1
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        weeklyData.forEachIndexed { index, value ->
            val isToday = index == todayIndex
            val hasData = value > 0f // Veri var mı kontrolü

            val barColor = if (isToday) {
                if (isDark) PrimaryGreen else PrimaryGreenDark
            } else {
                PrimaryGreen.copy(alpha = if (isDark) 0.15f else 0.25f)
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(horizontal = 6.dp)
                ) {
                    val barWidth = 10.dp.toPx()
                    // Eğer değer 0 ise, görsel bir taban oluşturmak için 4dp'lik sabit bir yükseklik veriyoruz
                    val barHeight = if (hasData) (value / maxVal) * size.height else 4.dp.toPx()
                    val cornerRes = 6.dp.toPx()

                    // Arka plan izi
                    drawRoundRect(
                        color = if (isDark) Color.White.copy(0.05f) else Color.Black.copy(0.05f),
                        size = Size(barWidth, size.height),
                        topLeft = Offset((size.width - barWidth) / 2, 0f),
                        cornerRadius = CornerRadius(cornerRes)
                    )

                    // Gerçek veri barı veya 0 göstergesi
                    drawRoundRect(
                        color = if (hasData) barColor else barColor.copy(alpha = 0.1f),
                        topLeft = Offset((size.width - barWidth) / 2, size.height - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(cornerRes)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = labels[index],
                    color = if (isToday) {
                        if (isDark) PrimaryGreen else PrimaryGreenDark
                    } else {
                        if (isDark) TextGrayLight else TextGrayDark
                    },
                    fontSize = 11.sp,
                    fontFamily = Manrope,
                    fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.Medium
                )
            }
        }
    }
}