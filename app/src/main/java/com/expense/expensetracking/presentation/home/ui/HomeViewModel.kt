package com.expense.expensetracking.presentation.home.ui

import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
): BaseViewModel<HomeState, HomeIntent>(
    initialState = HomeState()
) {

    init {
        viewModelScope.launch {

        }
    }
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