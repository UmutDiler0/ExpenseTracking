package com.expense.expensetracking.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PASSWORD = stringPreferencesKey("user_password")
        val IS_REMEMBER_ME = booleanPreferencesKey("is_remember_me")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val BALANCE_LIMIT = doublePreferencesKey("balance_limit")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val NOTIFICATION_SENT = booleanPreferencesKey("notification_sent")
        val LAST_NOTIFIED_BALANCE = doublePreferencesKey("last_notified_balance")
    }

    val onBoardingCompleted: Flow<Boolean> = context.dataStore.data.map {
        it[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
    }

    val email: Flow<String> = context.dataStore.data.map {
        it[PreferencesKeys.USER_EMAIL] ?: ""
    }

    val password: Flow<String> = context.dataStore.data.map {
        it[PreferencesKeys.USER_PASSWORD] ?: ""
    }

    val isRememberMe: Flow<Boolean> = context.dataStore.data.map {
        it[PreferencesKeys.IS_REMEMBER_ME] ?: false
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map {
        it[PreferencesKeys.IS_DARK_MODE] ?: false
    }

    val balanceLimit: Flow<Double> = context.dataStore.data.map {
        it[PreferencesKeys.BALANCE_LIMIT] ?: 0.0
    }

    val notificationEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[PreferencesKeys.NOTIFICATION_ENABLED] ?: false
    }

    val notificationSent: Flow<Boolean> = context.dataStore.data.map {
        it[PreferencesKeys.NOTIFICATION_SENT] ?: false
    }

    val lastNotifiedBalance: Flow<Double> = context.dataStore.data.map {
        it[PreferencesKeys.LAST_NOTIFIED_BALANCE] ?: 0.0
    }

    suspend fun saveOnBoardingStatus(completed: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.ONBOARDING_COMPLETED] = completed }
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { it[PreferencesKeys.USER_EMAIL] = email }
    }

    suspend fun savePassword(password: String) {
        context.dataStore.edit { it[PreferencesKeys.USER_PASSWORD] = password }
    }

    suspend fun saveRememberMe(isRemember: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_REMEMBER_ME] = isRemember }
    }

    suspend fun saveDarkMode(isDark: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_DARK_MODE] = isDark }
    }

    suspend fun saveBalanceLimit(limit: Double) {
        context.dataStore.edit { it[PreferencesKeys.BALANCE_LIMIT] = limit }
    }

    suspend fun saveNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.NOTIFICATION_ENABLED] = enabled }
    }

    suspend fun saveNotificationSent(sent: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.NOTIFICATION_SENT] = sent }
    }

    suspend fun saveLastNotifiedBalance(balance: Double) {
        context.dataStore.edit { it[PreferencesKeys.LAST_NOTIFIED_BALANCE] = balance }
    }

    suspend fun resetNotificationState() {
        context.dataStore.edit {
            it[PreferencesKeys.NOTIFICATION_SENT] = false
            it[PreferencesKeys.LAST_NOTIFIED_BALANCE] = 0.0
        }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit {
            it.remove(PreferencesKeys.USER_EMAIL)
            it.remove(PreferencesKeys.USER_PASSWORD)
            it.remove(PreferencesKeys.IS_REMEMBER_ME)
        }
    }
}