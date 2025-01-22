package com.example.clientaccesscontrol.injection

import android.content.Context
import android.util.Log
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.preference.UserPreference
import com.example.clientaccesscontrol.data.preference.dataStore
import com.example.clientaccesscontrol.data.retrofit.ConfigApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Data {
    fun provideCACRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val baseUrl = runBlocking { pref.getSession().first().ipAdress }
        val username = runBlocking { pref.getSession().first().username }
        val password = runBlocking { pref.getSession().first().password }

        try {
            val apiServiceCAC = ConfigApi.getApiServiceCAC()
            val apiServiceMikrotik = ConfigApi.getApiServiceMikrotik(baseUrl, username, password)

            return Repository.getInstance(pref, apiServiceCAC, apiServiceMikrotik)
        } catch (e: Exception) {
            Log.e("Data", "Error in provideCACRepository", e)
            throw e
        }
    }
}