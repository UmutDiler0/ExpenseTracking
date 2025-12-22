package com.expense.expensetracking.presentation.home.ui

import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.data.repo.AuthRepository
import com.expense.expensetracking.data.repo.FirestoreRepo
import com.expense.expensetracking.domain.model.ExpenseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepo: FirestoreRepo,
    private val userDao: UserDao
): BaseViewModel<HomeState, HomeIntent>(
    initialState = HomeState()
) {

    init {
        userDao.observeUser()
            .onEach { user ->
                user?.let {
                    handleDataState {
                        copy(
                            user = uiDataState.value.user.copy(
                                expenseList = it.expenseList,
                                totalBalance = it.totalBalance,
                                cardList = it.cardList
                            ),
                            uiState = UiState.Idle
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
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
            is HomeIntent.SelectCard -> {
                handleDataState {
                    copy(
                        selectedCardItem = intent.card
                    )
                }
            }
            is HomeIntent.SelectCategory -> {
                handleDataState {
                    copy(
                        selectedCategory = intent.category
                    )
                }
            }
            is HomeIntent.SetMenu -> {
                handleDataState {
                    copy(
                        currentMenuState = intent.menu
                    )
                }
            }
        }
    }

    fun addSpend() {
        viewModelScope.launch {
            val currentUiData = uiDataState.value
            val selectedItem = currentUiData.selectedCardItem
            val spendAmount = currentUiData.spendBalance.toIntOrNull() ?: 0

            if (selectedItem.name.isNotEmpty() && spendAmount > 0) {

                if (selectedItem.balance < spendAmount) {
                    return@launch
                }

                handleDataState { copy(uiState = UiState.Loading) }

                firestoreRepo.addSpend(
                    ExpenseItem(
                        title = currentUiData.selectedCategory.categoryName,
                        price = spendAmount,
                        priceUp = false,
                        iconName = currentUiData.selectedCategory.iconName,
                        spendOrAddCard = selectedItem
                    )
                )

                handleDataState { copy(uiState = UiState.Success) }
            }
        }
    }

    fun addBalance() {
        viewModelScope.launch {
            val currentUiData = uiDataState.value
            if (currentUiData.selectedCardItem.name.isNotEmpty() && currentUiData.addBalance.isNotEmpty()) {

                handleDataState { copy(uiState = UiState.Loading) }

                firestoreRepo.addBalance(
                    ExpenseItem(
                        title = "Gelir",
                        price = currentUiData.addBalance.toInt(),
                        priceUp = true,
                        spendOrAddCard = currentUiData.selectedCardItem
                    )
                )

                handleDataState {
                    copy(
                        uiState = UiState.Success,
                        addBalance = ""
                    )
                }
            }
        }
    }
}