package com.expense.expensetracking.presentation.profile.ui

import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.expense.expensetracking.BaseViewModel
import com.expense.expensetracking.common.util.UiState
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.data.manager.DataStoreManager
import com.expense.expensetracking.data.repo.AuthRepository
import com.expense.expensetracking.notification.BalanceCheckWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDao: UserDao,
    private val dataStoreManager: DataStoreManager,
    private val workManager: WorkManager
): BaseViewModel<ProfileState, ProfileIntent>(
    initialState = ProfileState()
){
    companion object {
        private const val BALANCE_CHECK_WORK_NAME = "balance_check_work"
    }

    init {
        viewModelScope.launch {
            combine(
                userDao.observeUser(),
                dataStoreManager.balanceLimit,
                dataStoreManager.notificationEnabled
            ) { user, limit, enabled ->
                Triple(user, limit, enabled)
            }.collect { (user, limit, enabled) ->
                handleDataState {
                    copy(
                        user = user,
                        balanceLimit = limit,
                        notificationEnabled = enabled,
                        uiState = if(user == null) UiState.Error("") else UiState.Idle
                    )
                }
            }
        }
    }

    override public fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.SetBalanceLimit -> setBalanceLimit(intent.limit)
            is ProfileIntent.ToggleNotification -> toggleNotification(intent.enabled)
            is ProfileIntent.ShowLimitDialog -> showLimitDialog(intent.show)
            is ProfileIntent.UpdateTempLimit -> updateTempLimit(intent.value)
        }
    }

    private fun setBalanceLimit(limit: Double) {
        viewModelScope.launch {
            dataStoreManager.saveBalanceLimit(limit)
            // Yeni limit belirlendiğinde bildirim durumunu sıfırla
            dataStoreManager.resetNotificationState()
            handleDataState {
                copy(
                    balanceLimit = limit,
                    showLimitDialog = false,
                    tempLimit = ""
                )
            }
            // Bildirim etkinse worker'ı yeniden planla
            if (uiDataState.value.notificationEnabled) {
                scheduleBalanceCheckWorker()
            }
        }
    }

    private fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveNotificationEnabled(enabled)
            // Bildirim açıldığında durumu sıfırla
            if (enabled) {
                dataStoreManager.resetNotificationState()
            }
            handleDataState {
                copy(notificationEnabled = enabled)
            }
            
            if (enabled) {
                scheduleBalanceCheckWorker()
            } else {
                cancelBalanceCheckWorker()
            }
        }
    }

    private fun showLimitDialog(show: Boolean) {
        handleDataState {
            copy(
                showLimitDialog = show,
                tempLimit = if (show) balanceLimit.toString() else ""
            )
        }
    }

    private fun updateTempLimit(value: String) {
        handleDataState {
            copy(tempLimit = value)
        }
    }

    private fun scheduleBalanceCheckWorker() {
        val workRequest = PeriodicWorkRequestBuilder<BalanceCheckWorker>(
            15, TimeUnit.MINUTES // Her 15 dakikada bir kontrol et (test için, production'da daha uzun olabilir)
        ).build()

        workManager.enqueueUniquePeriodicWork(
            BALANCE_CHECK_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun cancelBalanceCheckWorker() {
        workManager.cancelUniqueWork(BALANCE_CHECK_WORK_NAME)
    }

    fun logout(){
        viewModelScope.launch {
            handleDataState {
                copy(
                    uiState = UiState.Loading
                )
            }
            authRepository.logout()
            // Çıkış yaparken bildirimleri iptal et
            cancelBalanceCheckWorker()
            handleDataState {
                copy(
                    uiState = UiState.Success
                )
            }
        }
    }
}