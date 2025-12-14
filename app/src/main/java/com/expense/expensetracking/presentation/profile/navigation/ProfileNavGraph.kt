package com.expense.expensetracking.presentation.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.util.Profile
import com.expense.expensetracking.common.util.ProfileGraph
import com.expense.expensetracking.presentation.profile.ui.ProfileScreen

fun NavGraphBuilder.profileNavGraph(rootNnavController: NavHostController){
    navigation<ProfileGraph>(startDestination = Profile){
        composable<Profile>{
            ProfileScreen()
        }
    }
}