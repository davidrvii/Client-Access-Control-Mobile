package com.example.clientaccesscontrol.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERNAME_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[IP_ADDRESS_KEY] ?: "https://192.168.203.162/rest/",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun saveToken(token: String?) {
        if (token != null) {
            dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = token
                preferences[IS_LOGIN_KEY] = true
            }
        }
    }

    suspend fun saveBaseUrl(baseUrl: String) {
        val formattedUrl = when {
            baseUrl.startsWith("http://") && baseUrl.endsWith("/rest/") -> baseUrl
            baseUrl.startsWith("https://") && baseUrl.endsWith("/rest/") -> baseUrl
            else -> "https://$baseUrl/rest/"
        }

        dataStore.edit { preferences ->
            preferences[IP_ADDRESS_KEY] = formattedUrl
        }
    }

    suspend fun saveMikrotikLogin(username: String, password: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
            preferences[PASSWORD_KEY] = password
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val IP_ADDRESS_KEY = stringPreferencesKey("ip_address")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}