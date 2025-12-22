package com.expense.expensetracking.presentation.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.util.Auth
import com.expense.expensetracking.common.util.ForgotPassword
import com.expense.expensetracking.common.util.Login
import com.expense.expensetracking.common.util.MainGraph
import com.expense.expensetracking.common.util.Register
import com.expense.expensetracking.presentation.auth.fogot_password.ForgotPasswordScreen
import com.expense.expensetracking.presentation.auth.login.LoginScreen
import com.expense.expensetracking.presentation.auth.register.RegisterScreen

fun NavGraphBuilder. authNavGraph(navController: NavHostController){

    navigation<Auth>(startDestination = Login){
        composable<Login> {
            LoginScreen(
                onNavigateRegisterScreen = {
                    navController.navigate(Register){
                        popUpTo(Login) {
                            inclusive = true
                        }
                    }
                },
                onNavigateHomeScreen = {
                    navController.navigate(MainGraph){
                        popUpTo(Login) {
                            inclusive = true
                        }
                    }
                },
            ) {
                navController.navigate(ForgotPassword)
            }
        }

        composable<Register> {
            RegisterScreen(
                onNavigateHomeScreen = {
                    navController.navigate(MainGraph){
                        popUpTo(Register) {
                            inclusive = true
                        }
                    }
                }
            ) {
                navController.navigate(Login){
                    popUpTo(Register) {
                        inclusive = true
                    }
                }
            }
        }

        composable<ForgotPassword> {
            ForgotPasswordScreen {
                navController.popBackStack()
            }
        }
    }
}