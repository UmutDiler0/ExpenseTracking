package com.expense.expensetracking.presentation.auth.fogot_password

import com.expense.expensetracking.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FPViewModel @Inject constructor(): BaseViewModel<FPState, FPIntent>(
    initialState = FPState()
) {
    public override fun handleIntent(intent: FPIntent) {
        when(
            intent
        ){
            is FPIntent.SetEmail -> {
                handleDataState {
                    copy(
                        email = intent.email
                    )
                }
            }
            is FPIntent.Submit -> {}
        }
    }
}