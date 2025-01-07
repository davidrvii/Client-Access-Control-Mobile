package com.example.clientaccesscontrol.injection

import android.content.Context
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

        val apiServiceMikrotik = ConfigApi.getApiServiceMikrotik(baseUrl, username, password)
        val apiServiceCAC = ConfigApi.getApiServiceCAC()

        return Repository.getInstance(pref, apiServiceCAC, apiServiceMikrotik)
    }
}