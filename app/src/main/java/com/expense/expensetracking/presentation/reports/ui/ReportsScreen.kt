package com.expense.expensetracking.presentation.reports.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.expense.expensetracking.R
import com.expense.expensetracking.common.component.CustomTopAppBar
import com.expense.expensetracking.common.component.LoadingScreen
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.presentation.reports.component.CombinedReportContent
import com.expense.expensetracking.presentation.reports.component.ExpenseReportContent
import com.expense.expensetracking.presentation.reports.component.IncomeReportContent
import com.expense.expensetracking.presentation.reports.component.PeriodDropdown
import com.expense.expensetracking.ui.theme.BackgroundDark
import com.expense.expensetracking.ui.theme.BackgroundLight
import com.expense.expensetracking.ui.theme.ChartAmber
import com.expense.expensetracking.ui.theme.ChartPink
import com.expense.expensetracking.ui.theme.ChartSky
import com.expense.expensetracking.ui.theme.Manrope
import com.expense.expensetracking.ui.theme.PrimaryGreen
import com.expense.expensetracking.ui.theme.PrimaryGreenDark

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.uiDataState.collectAsStateWithLifecycle()

    when (state.uiState) {
        is UiState.Idle -> {
            ReportIdleScreen(
                viewModel,
                state
            )
        }

        is UiState.Success -> {}
        is UiState.Loading -> {
            LoadingScreen()
        }

        is UiState.Error -> {}
    }
}

@Composable
fun ReportIdleScreen(
    viewModel: ReportsViewModel,
    state: ReportsState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomTopAppBar(
            header = stringResource(R.string.reports_title),
            isBackBtnActive = false,
            isTrailingIconActive = false,
            icon = Icons.Default.Add
        ) { }

        // Tab Layout
        val tabs = listOf(
            stringResource(R.string.reports_tab_expense),
            stringResource(R.string.reports_tab_income),
            stringResource(R.string.reports_tab_combined)
        )
        val selectedTabIndex = when (state.selectedTab) {
            ReportTab.EXPENSE -> 0
            ReportTab.INCOME -> 1
            ReportTab.COMBINED -> 2
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        val newTab = when (index) {
                            0 -> ReportTab.EXPENSE
                            1 -> ReportTab.INCOME
                            else -> ReportTab.COMBINED
                        }
                        viewModel.handleIntent(ReportsIntent.ChangeTab(newTab))
                    },
                    text = {
                        Text(
                            text = title,
                            fontFamily = Manrope,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        PeriodDropdown(
            selectedPeriod = state.selectedPeriod,
            onPeriodSelected = { newPeriod ->
                viewModel.handleIntent(ReportsIntent.ChangePeriod(newPeriod))
            }
        )

        // Content based on selected tab
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            when (state.selectedTab) {
                ReportTab.EXPENSE -> {
                    item {
                        ExpenseReportContent(state, isSystemInDarkTheme())
                    }
                }

                ReportTab.INCOME -> {
                    item {
                        IncomeReportContent(state, isSystemInDarkTheme())
                    }
                }

                ReportTab.COMBINED -> {
                    item {
                        CombinedReportContent(state, isSystemInDarkTheme())
                    }
                }
            }
        }
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
    val colors =
        listOf(ChartAmber, ChartSky, ChartPink, PrimaryGreen, Color(0xFF8B5CF6), Color(0xFFF59E0B))
    return colors[index % colors.size]
}