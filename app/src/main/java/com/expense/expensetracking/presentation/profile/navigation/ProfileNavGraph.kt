package com.expense.expensetracking.presentation.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.navigation.MainNavGraph
import com.expense.expensetracking.common.util.Auth
import com.expense.expensetracking.common.util.MainGraph
import com.expense.expensetracking.common.util.Profile
import com.expense.expensetracking.common.util.ProfileGraph
import com.expense.expensetracking.common.util.Settings
import com.expense.expensetracking.presentation.profile.ui.ProfileScreen
import com.expense.expensetracking.presentation.settings.SettingsScreen

fun NavGraphBuilder.profileNavGraph(rootNnavController: NavHostController, bottomNavHostController: NavHostController){
    navigation<ProfileGraph>(startDestination = Profile){
        composable<Profile>{
            ProfileScreen(
                onNavigateSettingScreen = {
                    bottomNavHostController.navigate(Settings)
                }
            ){
                rootNnavController.navigate(
                    Auth
                ){
                    popUpTo(MainGraph) {
                        inclusive = true
                    }
                }
            }
        }

        composable<Settings> {
            SettingsScreen(
                onNavigateBack = {
                    bottomNavHostController.popBackStack()
                },
                onNavigateChangePassword = {}
            ){

            }
        }
    }
}