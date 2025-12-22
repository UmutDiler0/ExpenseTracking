package com.expense.expensetracking.presentation.profile.ui

import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDao: UserDao
): BaseViewModel<ProfileState, ProfileIntent>(
    initialState = ProfileState()
){
    init {
        viewModelScope.launch {
            val user = userDao.getUser()
            handleDataState {
                copy(
                    user = user,
                    uiState = if(user == null) UiState.Error("") else UiState.Idle
                )
            }
        }
    }
    override fun handleIntent(intent: ProfileIntent) {
        TODO("Not yet implemented")
    }

    fun logout(){
        viewModelScope.launch {
            handleDataState {
                copy(
                    uiState = UiState.Loading
                )
            }
            authRepository.logout()
            handleDataState {
                copy(
                    uiState = UiState.Success
                )
            }
        }
    }
}