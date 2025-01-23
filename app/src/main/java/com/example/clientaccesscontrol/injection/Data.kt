package com.example.clientaccesscontrol.injection

import android.content.Context
import android.util.Log
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.preference.UserPreference
import com.example.clientaccesscontrol.data.preference.dataStore
import com.example.clientaccesscontrol.data.retrofit.ConfigApi

object Data {
    fun provideCACRepository(context: Context): Repository {
        try {
            val pref = UserPreference.getInstance(context.dataStore)
            val apiServiceCAC = ConfigApi.getApiServiceCAC()
            val apiServiceMikrotik = ConfigApi.getApiServiceMikrotik(context)

            return Repository.getInstance(pref, apiServiceCAC, apiServiceMikrotik)
        } catch (e: Exception) {
            Log.e("Data", "Error in provideCACRepository", e)
            throw e
        }
    }
}