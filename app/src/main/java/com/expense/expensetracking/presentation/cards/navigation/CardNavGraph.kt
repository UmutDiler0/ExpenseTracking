package com.expense.expensetracking.presentation.cards.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.expense.expensetracking.common.util.AddCardScreen
import com.expense.expensetracking.common.util.CardDetail
import com.expense.expensetracking.common.util.CardGraph
import com.expense.expensetracking.common.util.Cards
import com.expense.expensetracking.presentation.cards.addcard.AddCardScreen
import com.expense.expensetracking.presentation.cards.card_detail.CardDetailScreen
import com.expense.expensetracking.presentation.cards.ui.CardsScreen

fun NavGraphBuilder.cardNavGraph(
    navController: NavHostController,
    bottomNavController: NavHostController
){

    navigation<CardGraph>(startDestination = Cards){
        composable<Cards> {
            CardsScreen(
                onNavigateCardDetail = {
                    bottomNavController.navigate(CardDetail)
                }
            ){
                bottomNavController.navigate(AddCardScreen)
            }
        }

        composable<CardDetail> {
            CardDetailScreen {
                navController.popBackStack()
            }
        }

        composable<AddCardScreen> {
            AddCardScreen {  }
        }
    }
}