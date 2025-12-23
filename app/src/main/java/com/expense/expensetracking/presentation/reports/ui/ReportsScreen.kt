package com.expense.expensetracking.presentation.reports.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.ui.theme.BorderColor
import com.expense.expensetracking.ui.theme.ChartAmber
import com.expense.expensetracking.ui.theme.ChartPink
import com.expense.expensetracking.ui.theme.ChartSky
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark
import com.expense.expensetracking.ui.theme.TextGray
import com.expense.expensetracking.ui.theme.TextWhite
import java.util.Calendar

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.uiDataState.collectAsStateWithLifecycle()


    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            CustomTopAppBar(header = "Raporlar", isBackBtnActive = false, isTrailingIconActive = false, icon = Icons.Default.Add) { }
        }

        // Period Selector
        item {
            PeriodSelector(
                selectedPeriod = state.selectedPeriod,
                onPeriodSelected = { newPeriod ->
                    viewModel.handleIntent(ReportsIntent.ChangePeriod(newPeriod))
                }
            )
        }

        // Stats Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(modifier = Modifier.weight(1f), title = "Toplam Harcama", value = "₺${state.totalExpense}")
                StatCard(modifier = Modifier.weight(1f), title = "Ortalama Harcama", value = "₺${state.averageExpense}")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            CustomBarChart(
                modifier = Modifier.height(200.dp).fillMaxWidth(),
                weeklyData = state.weeklyBarData
            )
        }

        // Donut Chart Bölümü (Dinamik Kategori Listesi)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = BorderStroke(1.dp, BorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Harcama Kategorileri", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomDonutChart(
                            modifier = Modifier.size(140.dp),
                            data = state.categoryDistribution
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            state.categoryDistribution.forEach { (category, amount) ->
                                CategoryLegendItem(category, "₺$amount", getCategoryColor(category))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodSelector(
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(Color(0xFF27272a).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("Günlük", "Haftalık", "Aylık").forEach { period ->
            val isSelected = selectedPeriod == period
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color(0xFF3f3f46) else Color.Transparent)
                    .clickable { onPeriodSelected(period) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = period,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CustomBarChart(
    modifier: Modifier = Modifier,
    weeklyData: List<Float>
) {
    val labels = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
    val maxVal = weeklyData.maxOrNull()?.takeIf { it > 0 } ?: 1f

    // Bugünün indexini bul (Pazartesi = 0, ..., Pazar = 6)
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
            val isToday = index == todayIndex // Bugün mü kontrolü

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    val barHeight = (value / maxVal) * size.height
                    val barWidth = 12.dp.toPx()

                    drawRoundRect(
                        // Sadece bugün ise koyu yeşil, değilse soluk yeşil
                        color = if (isToday) PrimaryGreen else PrimaryGreen.copy(alpha = 0.2f),
                        topLeft = Offset((size.width - barWidth) / 2, size.height - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = labels[index],
                    // Metin rengini de bugün ise beyaz/yeşil yapabilirsin
                    color = if (isToday) Color.White else TextGray,
                    fontSize = 12.sp,
                    fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CustomDonutChart(modifier: Modifier = Modifier, data: Map<String, Int>) {
    val total = data.values.sum().toFloat()

    Canvas(modifier = modifier) {
        var startAngle = -90f
        if (total == 0f) {
            drawArc(
                color = Color.Gray.copy(alpha = 0.2f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 35f)
            )
        } else {
            // Index yerine kategori adına (key) göre renk alıyoruz
            data.forEach { (categoryName, value) ->
                val sweepAngle = (value / total) * 360f
                drawArc(
                    color = getCategoryColor(categoryName), // Artık kategoriye özel renk basar
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 35f, cap = StrokeCap.Round)
                )
                startAngle += sweepAngle
            }
        }
    }
}

@Composable
fun CategoryLegendItem(name: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(12.dp))
        Text(name, color = Color(0xFFd4d4d8), fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text(value, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

fun getCategoryColor(category: String): Color {
    return when (category) {
        "Fatura" -> ChartAmber
        "Market" -> ChartSky
        "Ulaşım" -> ChartPink
        "Eğlence" -> Color(0xFF8B5CF6)
        "Sağlık" -> Color(0xFF10B981)
        else -> PrimaryGreen
    }
}

fun getCategoryColorByIndex(index: Int): Color {
    val colors = listOf(ChartAmber, ChartSky, ChartPink, PrimaryGreen, Color(0xFF8B5CF6), Color(0xFFF59E0B))
    return colors[index % colors.size]
}