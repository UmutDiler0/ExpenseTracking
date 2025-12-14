package com.expense.expensetracking.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.util.Home
import com.expense.expensetracking.common.util.HomeGraph
import com.expense.expensetracking.presentation.home.ui.HomeScreen

fun NavGraphBuilder.homeNavGraph(
    rootNavController: NavHostController
){

    navigation<HomeGraph>(startDestination = Home){
        composable<Home> {
            HomeScreen()
        }
    }
}