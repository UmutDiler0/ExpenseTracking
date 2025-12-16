package com.expense.expensetracking.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.util.AddBalance
import com.expense.expensetracking.common.util.Home
import com.expense.expensetracking.common.util.HomeGraph
import com.expense.expensetracking.common.util.SpendBalance
import com.expense.expensetracking.presentation.home.spend_balance.SpendBalanceScreen
import com.expense.expensetracking.presentation.home.ui.HomeScreen

fun NavGraphBuilder.homeNavGraph(
    rootNavController: NavHostController,
    bottomNavController: NavHostController
){

    navigation<HomeGraph>(startDestination = Home){
        composable<Home> {
            HomeScreen(
                onNavigateAddBalanceScreen = {
                    bottomNavController.navigate(AddBalance)
                }
            ){
                bottomNavController.navigate(SpendBalance)
            }
        }
        composable<AddBalance> {  }
        composable<SpendBalance> {
            SpendBalanceScreen(){
                bottomNavController.popBackStack()
            }
        }
    }
}