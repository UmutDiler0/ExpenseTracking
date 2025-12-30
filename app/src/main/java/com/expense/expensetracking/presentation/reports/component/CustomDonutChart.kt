package com.expense.expensetracking.presentation.reports.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import com.expense.expensetracking.presentation.reports.ui.getCategoryColor
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.TextBlack
import com.expense.expensetracking.ui.theme.TextGrayDark
import com.expense.expensetracking.ui.theme.TextGrayLight
import com.expense.expensetracking.ui.theme.TextWhite

@Composable
fun CustomDonutChart(
    modifier: Modifier = Modifier,
    data: Map<String, Int>
) {
    val isDark = isSystemInDarkTheme()
    val total = data.values.sum().toFloat()
    val strokeWidth = 32f

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f

            if (total == 0f) {
                drawArc(
                    color = if (isDark) Color.White.copy(0.1f) else Color.Black.copy(0.1f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            } else {
                data.forEach { (categoryName, value) ->
                    val sweepAngle = (value / total) * 360f
                    if (sweepAngle > 0f) {
                        drawArc(
                            color = getCategoryColor(categoryName),
                            startAngle = startAngle,
                            sweepAngle = sweepAngle - 2f, // Dilimler arası çok ince boşluk
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                    startAngle += sweepAngle
                }
            }
        }

        // UX: Donut Chart'ın ortasına bilgi ekleyerek alanı değerlendiriyoruz
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Toplam",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontFamily = Manrope
            )
            Text(
                text = "₺${total.toInt()}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontFamily = Manrope
            )
        }
    }
}