package com.expense.expensetracking.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.util.Auth
import com.expense.expensetracking.common.util.MainGraph
import com.expense.expensetracking.common.util.OnBoarding
import com.expense.expensetracking.common.util.Register
import com.expense.expensetracking.common.util.Splash
import com.expense.expensetracking.presentation.auth.navigation.authNavGraph
import com.expense.expensetracking.presentation.onboarding.ui.OnBoardingScreen
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
                onNavigateOnboardingScreen = {
                    navController.navigate(OnBoarding){
                        popUpTo(Splash) {
                            inclusive = true
                        }
                    }
                },
                onNavigateLoginScreen = {}
            ) { }
        }
        composable<OnBoarding>{
            OnBoardingScreen {
                navController.navigate(Auth){
                    popUpTo(OnBoarding) {
                        inclusive = true
                    }
                }
            }
        }
        authNavGraph(navController)

        composable<MainGraph> {
            MainNavGraph(navController)
        }
    }
}