package com.expense.expensetracking.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.expense.expensetracking.MainActivity
import com.expense.expensetracking.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "balance_limit_channel"
        const val CHANNEL_NAME = "Bakiye Limiti Bildirimleri"
        const val CHANNEL_DESCRIPTION = "Bakiye limitinize yaklaştığınızda bildirim alın"
        const val NOTIFICATION_ID = 1001
        
        const val DEEP_LINK_SCHEME = "expensetracking"
        const val DEEP_LINK_HOST = "profile"
        const val DEEP_LINK_PATH_LIMIT = "limit"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun sendBalanceLimitWarning(currentBalance: Double, limit: Double) {
        if (!hasNotificationPermission()) {
            return
        }

        try {
            // Deeplink intent oluştur
            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("$DEEP_LINK_SCHEME://$DEEP_LINK_HOST/$DEEP_LINK_PATH_LIMIT"),
                context,
                MainActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                deepLinkIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("⚠️ Limitinize Yaklaşıyorsunuz!")
                .setContentText("Toplam bakiyeniz ${String.format("%.2f", currentBalance)} TL. Belirlediğiniz ${String.format("%.2f", limit)} TL limitine yaklaştınız.")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Mevcut bakiyeniz ${String.format("%.2f", currentBalance)} TL. Belirlediğiniz ${String.format("%.2f", limit)} TL limitine 500 TL'den az kaldı. Harcamalarınızı kontrol etmek için tıklayın.")
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notificationManager.notify(NOTIFICATION_ID, notification)
                }
            } else {
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            // Bildirim gönderilemezse sessizce geç
        }
    }
}
