package com.expense.expensetracking.presentation.home.ui

import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.data.manager.DataStoreManager
import com.expense.expensetracking.data.repo.AuthRepository
import com.expense.expensetracking.data.repo.FirestoreRepo
import com.expense.expensetracking.domain.model.ExpenseItem
import com.expense.expensetracking.notification.NotificationHelper
import com.expense.expensetracking.presentation.home.component.Menu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepo: FirestoreRepo,
    private val userDao: UserDao,
    private val dataStoreManager: DataStoreManager,
    private val notificationHelper: NotificationHelper
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
                            filteredExpenses = filterExpenses(it.expenseList, uiDataState.value.selectedCardFilter),
                            uiState = UiState.Idle
                        )
                    }
                    // Bakiye değiştiğinde bildirim kontrolü yap
                    checkBalanceLimit(it.totalBalance)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun checkBalanceLimit(currentBalance: Int) {
        viewModelScope.launch {
            val notificationEnabled = dataStoreManager.notificationEnabled.first()
            val balanceLimit = dataStoreManager.balanceLimit.first()
            val notificationSent = dataStoreManager.notificationSent.first()
            
            if (notificationEnabled && balanceLimit > 0) {
                val currentBalanceDouble = currentBalance.toDouble()
                val difference = currentBalanceDouble - balanceLimit
                
                // Bakiye yeterince artmışsa (limite 1000 TL'den fazla uzaklaşmışsa) bildirimi sıfırla
                if (difference > 1000.0 && notificationSent) {
                    dataStoreManager.resetNotificationState()
                    return@launch
                }
                
                // Bakiye limitin üzerinde ve limite 500 TL'den az mesafede ise bildirim gönder
                // Örnek: Limit 600 TL, bakiye 700 TL ise fark 100 TL (bildirim gönderilir)
                // Örnek: Limit 600 TL, bakiye 400 TL ise fark -200 TL (bildirim gönderilmez, zaten limiti geçmiş)
                if (difference > 0 && difference <= 500.0 && !notificationSent) {
                    notificationHelper.sendBalanceLimitWarning(currentBalanceDouble, balanceLimit)
                    // Bildirim gönderildi olarak işaretle
                    dataStoreManager.saveNotificationSent(true)
                    dataStoreManager.saveLastNotifiedBalance(currentBalanceDouble)
                }
            }
        }
    }

    private fun filterExpenses(expenses: List<ExpenseItem>, selectedCard: String?): List<ExpenseItem> {
        return if (selectedCard == null) {
            expenses // Tümü seçili
        } else {
            expenses.filter { it.spendOrAddCard.name == selectedCard }
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
            is HomeIntent.SelectCard -> {
                handleDataState {
                    copy(
                        selectedCardItem = intent.card,
                        currentMenuState = Menu.IDLE // Kart seçilince menüyü kapat
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
                // Liste boşsa menüyü açma, IDLE tut
                if (intent.menu == Menu.CARDS && uiDataState.value.user.cardList.isEmpty()) {
                    // UI tarafında Toast tetiklenebilir veya state üzerinden hata verilebilir
                    return
                }
                handleDataState { copy(currentMenuState = intent.menu) }
            }
            is HomeIntent.FilterByCard -> {
                handleDataState {
                    copy(
                        selectedCardFilter = intent.cardName,
                        filteredExpenses = filterExpenses(user.expenseList, intent.cardName)
                    )
                }
            }
        }
    }

    fun addSpend() {
        viewModelScope.launch {
            try {
                val currentUiData = uiDataState.value
                val selectedItem = currentUiData.selectedCardItem
                // Noktaları kaldırarak sayıya çevir
                val spendAmount = currentUiData.spendBalance.replace(".", "").toIntOrNull() ?: 0

                if (selectedItem.name.isEmpty()) {
                    handleDataState { copy(uiState = UiState.Error("")) }
                    return@launch
                }

                if (spendAmount <= 0) {
                    handleDataState { copy(uiState = UiState.Error("")) }
                    return@launch
                }

                // Bakiye kontrolü kaldırıldı - artık dialog ile onay alınıyor

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

                handleDataState { 
                    copy(
                        uiState = UiState.Success,
                        spendBalance = "",
                        selectedCategory = com.expense.expensetracking.domain.model.Category("", ""),
                        selectedCardItem = com.expense.expensetracking.domain.model.CardItem()
                    )
                }
            } catch (e: Exception) {
                handleDataState { copy(uiState = UiState.Error("")) }
            }
        }
    }

    fun addBalance() {
        viewModelScope.launch {
            try {
                val currentUiData = uiDataState.value
                
                if (currentUiData.selectedCardItem.name.isEmpty()) {
                    handleDataState { copy(uiState = UiState.Error("")) }
                    return@launch
                }
                
                // Noktaları kaldırarak sayıya çevir
                val balanceAmount = currentUiData.addBalance.replace(".", "").toIntOrNull()
                if (balanceAmount == null || balanceAmount <= 0) {
                    handleDataState { copy(uiState = UiState.Error("")) }
                    return@launch
                }

                handleDataState { copy(uiState = UiState.Loading) }

                firestoreRepo.addBalance(
                    ExpenseItem(
                        title = "Gelir",
                        price = balanceAmount,
                        priceUp = true,
                        spendOrAddCard = currentUiData.selectedCardItem
                    )
                )

                handleDataState {
                    copy(
                        uiState = UiState.Success,
                        addBalance = "",
                        selectedCardItem = com.expense.expensetracking.domain.model.CardItem()
                    )
                }
            } catch (e: Exception) {
                handleDataState { copy(uiState = UiState.Error("")) }
            }
        }
    }

    fun resetUiState() {
        handleDataState { copy(uiState = UiState.Idle) }
    }
}