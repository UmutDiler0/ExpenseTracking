package com.expense.expensetracking.presentation.home.ui

import com.expense.expensetracking.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): BaseViewModel<HomeState, HomeIntent>(
    initialState = HomeState()
) {
    override fun handleIntent(intent: HomeIntent) {
        TODO("Not yet implemented")
    }
}