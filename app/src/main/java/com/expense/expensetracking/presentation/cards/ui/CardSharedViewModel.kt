package com.expense.expensetracking.presentation.cards.ui

import android.util.Log
import androidx.compose.material3.Card
import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.data.repo.FirestoreRepo
import com.expense.expensetracking.domain.model.CardItem
import com.expense.expensetracking.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardSharedViewModel @Inject constructor(
    private val userDao: UserDao,
    private val firestoreRepo: FirestoreRepo,
): BaseViewModel<SharedCardState, SharedCardIntent>(
    initialState = SharedCardState()
) {

    init {
        observeUserFromDB()
    }

    override public fun handleIntent(intent: SharedCardIntent) {
        when(intent){
            is SharedCardIntent.SetCardName -> {
                handleDataState {
                    copy(
                        addCardName = intent.name,
                        cardNameError = null // Kullanıcı yazarken hatayı temizle
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
            is SharedCardIntent.SetCurrentCard -> {
                handleDataState {
                    copy(
                        currentCard = intent.card,
                    )
                }
            }
            is SharedCardIntent.SetCurrentCardStage -> {
                handleDataState {
                    copy(
                        currentCardStage = intent.stage
                    )
                }
            }
        }
    }

    fun addCardToDB() {
        viewModelScope.launch {
            val name = uiDataState.value.addCardName
            val balance = uiDataState.value.addCardBalance
            val currentCards = uiDataState.value.user.cardList

            if (name.isNotEmpty() && balance.isNotEmpty()) {

                val isCardExist = currentCards.any { it.name.equals(name, ignoreCase = true) }

                if (isCardExist) {
                    // Kullanıcıya hata mesajı göster
                    handleDataState {
                        copy(
                            cardNameError = "Bu isimde bir kart zaten mevcut"
                        )
                    }
                    return@launch
                }

                handleDataState { copy(uiState = UiState.Loading) }

                val newCard = CardItem(
                    name = name,
                    balance = balance.replace(".", "").toInt()
                )

                try {
                    firestoreRepo.addCard(newCard)
                    
                    handleDataState {
                        copy(
                            uiState = UiState.Success,
                            addCardName = "",
                            addCardBalance = "",
                            cardNameError = null
                        )
                    }
                } catch (e: Exception) {
                    Log.e("ViewModel", "Kart ekleme hatası: ${e.message}")
                    handleDataState {
                        copy(
                            uiState = UiState.Error(e.message ?: "Bilinmeyen hata")
                        )
                    }
                }
            }
        }
    }

    fun deleteCard(cardItem: CardItem) {
        viewModelScope.launch {
            handleDataState { copy(uiState = UiState.Loading) }
            
            try {
                firestoreRepo.removeCard(cardItem)
                
                handleDataState { copy(uiState = UiState.Idle) }
            } catch (e: Exception) {
                Log.e("ViewModel", "Kart silme hatası: ${e.message}")
                handleDataState {
                    copy(
                        uiState = UiState.Error(e.message ?: "Bilinmeyen hata")
                    )
                }
            }
        }
    }

    private fun observeUserFromDB() {
        userDao.observeUser()
            .onEach { user ->
                val currentUser = user ?: User(surname = "")
                handleDataState {
                    copy(
                        user = currentUser,
                        cardList = currentUser.cardList,
                        uiState = UiState.Idle
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun getUserFromDB(){
        viewModelScope.launch {
            val user = userDao.getUser() ?: User()
            handleDataState {
                copy(
                    user = user,
                    uiState = UiState.Idle,
                    cardList = user.cardList
                )
            }
        }
    }
}