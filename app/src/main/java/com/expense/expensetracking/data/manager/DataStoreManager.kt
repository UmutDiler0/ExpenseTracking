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

    suspend fun clearAuthData() {
        context.dataStore.edit {
            it.remove(PreferencesKeys.USER_EMAIL)
            it.remove(PreferencesKeys.USER_PASSWORD)
            it.remove(PreferencesKeys.IS_REMEMBER_ME)
        }
    }
}