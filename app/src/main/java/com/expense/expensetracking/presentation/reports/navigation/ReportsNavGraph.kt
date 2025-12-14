package com.expense.expensetracking.presentation.reports.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.util.Reports
import com.expense.expensetracking.common.util.ReportsGraph
import com.expense.expensetracking.presentation.reports.ui.ReportsScreen

fun NavGraphBuilder.reportNavGraph(rootNavGraph: NavHostController){
    navigation<ReportsGraph>(startDestination = Reports){
        composable<Reports> {
            ReportsScreen()
        }
    }
}