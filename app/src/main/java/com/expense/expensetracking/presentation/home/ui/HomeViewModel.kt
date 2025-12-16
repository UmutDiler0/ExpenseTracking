package com.expense.expensetracking.presentation.home.ui

import com.expense.expensetracking.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): BaseViewModel<HomeState, HomeIntent>(
    initialState = HomeState()
) {
    override public fun handleIntent(intent: HomeIntent) {
        when(intent){
            is HomeIntent.AddSpendValue -> {
                handleDataState {
                    copy(
                        spendBalance = intent.value
                    )
                }
            }
            is HomeIntent.AddBalanceValue -> {
                handleDataState {
                    copy(
                        addBalance = intent.value
                    )
                }
            }
        }
    }
}