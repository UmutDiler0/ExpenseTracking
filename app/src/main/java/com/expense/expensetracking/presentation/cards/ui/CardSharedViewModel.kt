package com.expense.expensetracking.presentation.cards.ui

import com.expense.expensetracking.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardSharedViewModel @Inject constructor(): BaseViewModel<SharedCardState, SharedCardIntent>(
    initialState = SharedCardState()
) {

    override fun handleIntent(intent: SharedCardIntent) {
        when(intent){
            is SharedCardIntent.SetCardName -> {
                handleDataState {
                    copy(
                        addCardName = intent.name
                    )
                }
            }
            is SharedCardIntent.SetCardBalance -> {
                handleDataState {
                    copy(
                        addCardBalance = intent.balance
                    )
                }
            }
            is SharedCardIntent.AddBalance -> {
                handleDataState {
                    copy(
                        addBalance = intent.value
                    )
                }
            }
        }
    }
}