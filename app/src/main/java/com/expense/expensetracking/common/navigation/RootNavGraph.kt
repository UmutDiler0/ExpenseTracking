package com.expense.expensetracking.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.expense.expensetracking.common.util.Splash
import com.expense.expensetracking.presentation.splash.SplashScreen

@Composable
fun RootNavController(
    navController: NavHostController,
){
    NavHost(
        navController = navController,
        startDestination = Splash
    ){
        composable<Splash> {
            SplashScreen(
                onNavigateOnboardingScreen = {},
                onNavigateLoginScreen = {}
            ) { }
        }
    }
}