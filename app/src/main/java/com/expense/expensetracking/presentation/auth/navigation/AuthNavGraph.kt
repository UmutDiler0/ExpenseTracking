package com.expense.expensetracking.presentation.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.util.Auth
import com.expense.expensetracking.common.util.ForgotPassword
import com.expense.expensetracking.common.util.Login
import com.expense.expensetracking.common.util.Register
import com.expense.expensetracking.presentation.auth.login.LoginScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController){

    navigation<Auth>(startDestination = Login){
        composable<Login> {
            LoginScreen(
                onNavigateRegisterScreen = {},
                onNavigateHomeScreen = {},
            ) { }
        }

        composable<Register> {  }

        composable<ForgotPassword> {  }
    }
}