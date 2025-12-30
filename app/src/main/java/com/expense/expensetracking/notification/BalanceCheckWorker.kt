package com.expense.expensetracking.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.data.manager.DataStoreManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class BalanceCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userDao: UserDao,
    private val dataStoreManager: DataStoreManager,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Kullanıcı bilgilerini ve limiti al
            val user = userDao.getUser() ?: return Result.success()
            val balanceLimit = dataStoreManager.balanceLimit.firstOrNull() ?: return Result.success()
            val notificationSent = dataStoreManager.notificationSent.firstOrNull() ?: false
            
            // Limit belirlenmemişse (0 veya negatif) kontrol yapma
            if (balanceLimit <= 0) {
                return Result.success()
            }

            val currentBalance = user.totalBalance
            val currentBalanceDouble = currentBalance.toDouble()
            val difference = currentBalanceDouble - balanceLimit
            
            // Bakiye yeterince artmışsa (limite 1000 TL'den fazla uzaklaşmışsa) bildirimi sıfırla
            if (difference > 1000.0 && notificationSent) {
                dataStoreManager.resetNotificationState()
                return Result.success()
            }
            
            // Bakiye limitin üzerinde, limite 500 TL'den az mesafede ve daha önce gönderilmemişse bildirim gönder
            if (difference > 0 && difference <= 500.0 && !notificationSent) {
                notificationHelper.sendBalanceLimitWarning(currentBalanceDouble, balanceLimit)
                dataStoreManager.saveNotificationSent(true)
                dataStoreManager.saveLastNotifiedBalance(currentBalanceDouble)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
