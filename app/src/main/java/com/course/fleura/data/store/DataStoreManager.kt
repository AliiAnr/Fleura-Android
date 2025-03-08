package com.course.fleura.data.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {

    /** Menyimpan status onboarding */
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    /** Mendapatkan status onboarding */
    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[ONBOARDING_COMPLETED_KEY] ?: false }

    /** Menyimpan status login */
    suspend fun setUserLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_LOGGED_IN_KEY] = loggedIn
        }
    }

    /** Mendapatkan status login */
    val isUserLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[USER_LOGGED_IN_KEY] ?: false }

    /** Menyimpan token pengguna */
    suspend fun saveUserToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }

    /** Mendapatkan token pengguna */
    val userToken: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_TOKEN_KEY] }

    /** Menghapus semua data (Logout) */
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Keys untuk DataStore
    companion object {
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
        private val USER_LOGGED_IN_KEY = booleanPreferencesKey("user_logged_in")
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
    }
}