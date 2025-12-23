package com.expense.expensetracking.common.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatTimestamp(timestamp: com.google.firebase.Timestamp): String {
    val date = timestamp.toDate()
    val now = Calendar.getInstance()
    val spendDate = Calendar.getInstance().apply { time = date }

    // Zaman farklarını milisaniye cinsinden hesapla
    val diffMillis = now.timeInMillis - spendDate.timeInMillis
    val diffDays = diffMillis / (24 * 60 * 60 * 1000)

    return when {
        // Bugün (Yılın günü ve yıl aynı mı?)
        now.get(Calendar.YEAR) == spendDate.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == spendDate.get(Calendar.DAY_OF_YEAR) -> "Bugün"

        // Dün
        diffDays == 1L -> "Dün"

        // 1 Hafta Önce (7-13 gün arası)
        diffDays in 7..13 -> "1 hafta önce"

        // 2 Hafta Önce (14-20 gün arası)
        diffDays in 14..20 -> "2 hafta önce"

        // 1 Ay Önce (Yaklaşık 30 gün)
        diffDays in 30..59 -> "1 ay önce"

        // Diğer durumlar için normal tarih formatı
        else -> {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("tr")) // "23 Aralık 2025" gibi
            sdf.format(date)
        }
    }
}