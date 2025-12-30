package com.expense.expensetracking.presentation.reports.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.expensetracking.R
import com.expense.expensetracking.presentation.reports.ui.ReportsState
import com.expense.expensetracking.ui.theme.BorderColor
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.PrimaryGreenDark
import com.expense.expensetracking.ui.theme.SurfaceDark
import com.expense.expensetracking.ui.theme.TextBlack
import com.expense.expensetracking.ui.theme.TextGrayDark
import com.expense.expensetracking.ui.theme.TextGrayLight
import com.expense.expensetracking.ui.theme.TextWhite

@Composable
fun CombinedReportContent(state: ReportsState, isDark: Boolean) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.reports_total_income),
                value = "₺${state.totalIncome}"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.reports_total_expense),
                value = "₺${state.totalExpense}"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (state.totalIncome == 0 && state.totalExpense == 0) {
            EmptyReportPlaceholder(
                message = "Bu dönemde hiçbir işlem bulunamadı",
                isDark = isDark
            )
        } else {
            // Net değer kartı
            val netValue = state.totalIncome - state.totalExpense
            val netColor = if (netValue >= 0) {
                if (isDark) PrimaryGreen else PrimaryGreenDark
            } else {
                Color(0xFFEF4444)
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.reports_net_status),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontFamily = Manrope,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "₺$netValue",
                        color = netColor,
                        fontSize = 32.sp,
                        fontFamily = Manrope,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val pagerState = rememberPagerState(pageCount = { 3 })
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                            ) { page ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                when (page) {
                                    0 -> {
                                        Column {
                                            Text(
                                                text = stringResource(R.string.reports_income_chart),
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                fontSize = 16.sp,
                                                fontFamily = Manrope,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            CustomBarChart(
                                                modifier = Modifier
                                                    .height(200.dp)
                                                    .fillMaxWidth(),
                                                weeklyData = state.weeklyIncomeData
                                            )
                                        }
                                    }
                                    1 -> {
                                        Column {
                                            Text(
                                                text = stringResource(R.string.reports_expense_chart),
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                fontSize = 16.sp,
                                                fontFamily = Manrope,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            CustomBarChart(
                                                modifier = Modifier
                                                    .height(200.dp)
                                                    .fillMaxWidth(),
                                                weeklyData = state.weeklyBarData
                                            )
                                        }
                                    }
                                    2 -> {
                                        Column {
                                            Text(
                                                text = "Kategori Karşılaştırması",
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                fontSize = 16.sp,
                                                fontFamily = Manrope,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(1f),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Text(
                                                        text = stringResource(R.string.reports_tab_income),
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                        fontSize = 14.sp,
                                                        fontFamily = Manrope,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Spacer(modifier = Modifier.height(12.dp))
                                                    CustomDonutChart(
                                                        modifier = Modifier.size(120.dp),
                                                        data = state.incomeDistribution
                                                    )
                                                }
                                                Column(
                                                    modifier = Modifier.weight(1f),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Text(
                                                        text = stringResource(R.string.reports_tab_expense),
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                        fontSize = 14.sp,
                                                        fontFamily = Manrope,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Spacer(modifier = Modifier.height(12.dp))
                                                    CustomDonutChart(
                                                        modifier = Modifier.size(120.dp),
                                                        data = state.categoryDistribution
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Page indicator
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(3) { iteration ->
                            val color = if (pagerState.currentPage == iteration) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            }
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}