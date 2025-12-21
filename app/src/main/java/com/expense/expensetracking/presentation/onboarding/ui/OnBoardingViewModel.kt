package com.expense.expensetracking.presentation.onboarding.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.expensetracking.data.manager.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            dataStoreManager.saveOnBoardingStatus(true)
        }
    }
}