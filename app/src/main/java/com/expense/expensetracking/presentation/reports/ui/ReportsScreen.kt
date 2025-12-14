package com.expense.expensetracking.presentation.reports.ui

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.ui.theme.BorderColor
import com.expense.expensetracking.ui.theme.ChartAmber
import com.expense.expensetracking.ui.theme.ChartPink
import com.expense.expensetracking.ui.theme.ChartSky
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.SurfaceDark
import com.expense.expensetracking.ui.theme.TextGray
import com.expense.expensetracking.ui.theme.TextWhite

@Composable
fun ReportsScreen(

){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header
        item {
            HeaderSection(title = "Giderler", showPlus = true)
        }

        // Segmented Control
        item {
            var selectedPeriod by remember { mutableStateOf("Haftalık") }
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
                            .clickable { selectedPeriod = period },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period,
                            color = if (isSelected) TextWhite else TextGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Stats Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(modifier = Modifier.weight(1f), title = "Toplam Harcama", value = "₺860,50")
                StatCard(modifier = Modifier.weight(1f), title = "Ortalama Harcama", value = "₺122,92")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Weekly Bar Chart
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Haftalık Harcama Grafiği", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text("₺860,50", fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                        Text("Geçen haftaya göre", color = TextGray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier
                            .background(PrimaryGreen.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("+15.2%", color = PrimaryGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomBarChart(modifier = Modifier.height(200.dp).fillMaxWidth())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Categories Donut Chart
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Harcama Kategorileri", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                            CustomDonutChart(modifier = Modifier.size(160.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("TOPLAM", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                Text("₺860", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            CategoryLegendItem("Fatura", "₺344,20", ChartAmber)
                            CategoryLegendItem("Market", "₺258,15", ChartSky)
                            CategoryLegendItem("Ulaşım", "₺172,10", ChartPink)
                            CategoryLegendItem("Diğer", "₺86,05", PrimaryGreen)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(title: String, showPlus: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(androidx.compose.material.icons.Icons.Default.ChevronLeft, contentDescription = "Back", tint = Color(0xFFd4d4d8))
        }
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        if (showPlus) {
            IconButton(onClick = {}) {
                Icon(androidx.compose.material.icons.Icons.Default.Add, contentDescription = "Add", tint = TextWhite)
            }
        } else {
            Spacer(modifier = Modifier.size(48.dp)) // Dengelemek için boşluk
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
fun CustomBarChart(modifier: Modifier = Modifier) {
    val data = listOf(450f, 200f, 350f, 600f, 300f, 860f, 400f)
    val labels = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
    val maxVal = data.maxOrNull() ?: 1f

    Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 2)
        val spacing = size.width / data.size

        data.forEachIndexed { index, value ->
            val barHeight = (value / maxVal) * size.height
            val x = (spacing * index) + (spacing / 2) - (barWidth / 2)
            val y = size.height - barHeight

            // Bar çizimi
            drawRoundRect(
                color = if (index == 5) PrimaryGreen else PrimaryGreen.copy(alpha = 0.2f),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(6.dp.toPx())
            )

            // Label çizimi (Burada basitlik adına text çizimi yapılmadı, normalde nativeCanvas kullanılır)
        }
    }
    // Basit labels
    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        labels.forEach { Text(it, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun CustomDonutChart(modifier: Modifier = Modifier) {
    // Veriler: Amber, Sky, Pink, Green
    val proportions = listOf(0.4f, 0.3f, 0.2f, 0.1f)
    val colors = listOf(ChartAmber, ChartSky, ChartPink, PrimaryGreen)

    Canvas(modifier = modifier) {
        var startAngle = -90f
        val strokeWidth = 30f // Donut kalınlığı

        proportions.forEachIndexed { index, percent ->
            val sweepAngle = percent * 360f
            drawArc(
                color = colors[index],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += sweepAngle
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