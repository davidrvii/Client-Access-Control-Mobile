package com.example.clientaccesscontrol.data.preference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.clientaccesscontrol.data.cacresponse.CreateBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateModeResponse
import com.example.clientaccesscontrol.data.cacresponse.CreatePresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteClientResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteModeResponse
import com.example.clientaccesscontrol.data.cacresponse.DeletePresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.GetAccessResponse
import com.example.clientaccesscontrol.data.cacresponse.GetAllClientResponse
import com.example.clientaccesscontrol.data.cacresponse.GetBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.GetChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.GetClientDetailResponse
import com.example.clientaccesscontrol.data.cacresponse.GetModeResponse
import com.example.clientaccesscontrol.data.cacresponse.GetPresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.GetRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.GetSpeedResponse
import com.example.clientaccesscontrol.data.cacresponse.RegisterResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateClientDetailResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateNetworkResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.data.retrofit.ServiceApiCAC
import com.example.clientaccesscontrol.data.retrofit.ServiceApiMikrotik
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiServiceCAC: ServiceApiCAC,
    private val apiServiceMikrotik: ServiceApiMikrotik,
) {

    private suspend fun saveToken(token: String) {
        Log.d("UserRepository", "Token Saved in Session: $token")
        userPreference.saveToken(token)
    }

    private suspend fun saveUrlBase(baseUrl: String) {
        Log.d("UserRepository", "Base URL Saved in Session: $baseUrl")
        userPreference.saveBaseUrl(baseUrl)
    }

    private suspend fun saveMikortikLogin(username: String, password: String) {
        Log.d("UserRepository", "Username & Password for Mikrotik Login : $username & $password")
        userPreference.saveMikrotikLogin(username, password)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun register(
        username: String,
        password: String,
        ipAddress: String,
    ): RegisterResponse {
        return apiServiceCAC.register(username, password, ipAddress)
    }

    suspend fun login(
        ipAddress: String,
        username: String,
        password: String,
    ) {
        val loginResponse = apiServiceCAC.login(ipAddress, username, password)

        loginResponse.loginResult?.token?.let {
            Log.d("UserRepository", "Saving token: $it")
            saveToken(it)
        }
        loginResponse.loginResult?.ipAddress?.let {
            Log.d("UserRepository", "Saving base URL: $it")
            saveUrlBase(it)
        }
        loginResponse.loginResult?.let {
            Log.d("UserRepository", "Saving username & password: ${it.username} & ${it.password}")
            saveMikortikLogin(it.username.toString(), it.password.toString())
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun createBTS(
        token: String,
        bts: String,
    ): LiveData<Results<CreateBTSResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createBTS(token, bts)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
            Log.d("UserRepository", "Error creating BTS: ${e.message}")
        }
    }

    suspend fun createRadio(
        token: String,
        radio: String,
    ): LiveData<Results<CreateRadioResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createRadio(token, radio)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun createMode(
        token: String,
        mode: String,
    ): LiveData<Results<CreateModeResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createMode(token, mode)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun createPresharedKey(
        token: String,
        presharedKey: String,
    ): LiveData<Results<CreatePresharedKeyResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createPresharedKey(token, presharedKey)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun createChannelWidth(
        token: String,
        channelWidth: String,
    ): LiveData<Results<CreateChannelWidthResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createChannelWidth(token, channelWidth)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getQueueTree(): LiveData<Results<List<GetQueueTreeResponseItem>>> = liveData {
        emit(Results.Loading)
        try {
            Log.d("Repository", "Calling getQueueTree()")
            val response = apiServiceMikrotik.getQueueTree()
            Log.d("Repository", "Queue Tree Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error: ${e.message}")
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getBTS(
        token: String,
    ): LiveData<Results<GetBTSResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getBTS("Bearer $token")
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getRadio(
        token: String,
    ): LiveData<Results<GetRadioResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getRadio("Bearer $token")
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getMode(
        token: String,
    ): LiveData<Results<GetModeResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getMode("Bearer $token")
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getChannelWidth(
        token: String,
    ): LiveData<Results<GetChannelWidthResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getChannelWidth("Bearer $token")
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getPresharedKey(
        token: String,
    ): LiveData<Results<GetPresharedKeyResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getPresharedKey("Bearer $token")
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun deleteBTS(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteBTSResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteBTS("Bearer $token", id)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun deleteMode(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteModeResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteMode("Bearer $token", id)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun deleteRadio(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteRadioResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteRadio("Bearer $token", id)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun deleteChannelWidth(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteChannelWidthResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteChannelWidth("Bearer $token", id)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun deletePresharedKey(
        token: String,
        id: Int,
    ): LiveData<Results<DeletePresharedKeyResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deletePresharedKey("Bearer $token", id)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getAccess(
        token: String,
    ): LiveData<Results<GetAccessResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getAccess("Bearer $token")
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getSpeed(
        token: String,
    ): LiveData<Results<GetSpeedResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getSpeed("Bearer $token")
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getAllClient(
        token: String,
    ): LiveData<Results<GetAllClientResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getAllClient("Bearer $token")
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun getClientDetail(
        token: String,
        id: Int,
    ): LiveData<Results<GetClientDetailResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getClientDetail("Bearer $token", id)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    suspend fun updateNetwork(
        token: String,
        id: Int,
        radioName: String,
        frequency: String,
        ipRadio: String,
        ipAddress: String,
        wlanMacAddress: String,
        ssid: String,
        radioSignal: String,
        apLocation: String,
        radio: Int,
        mode: Int,
        channelWidth: Int,
        presharedKey: Int,
        comment: String,
        password: String,
        bts: Int,
    ): UpdateNetworkResponse {
        return apiServiceCAC.updateNetwork(
            token,
            id,
            radioName,
            frequency,
            ipRadio,
            ipAddress,
            wlanMacAddress,
            ssid,
            radioSignal,
            apLocation,
            radio,
            mode,
            channelWidth,
            presharedKey,
            comment,
            password,
            bts
        )
    }

    suspend fun updateClient(
        token: String,
        id: Int,
        access: Int,
        speed: Int,
    ): UpdateClientDetailResponse {
        return apiServiceCAC.updateClient(
            token,
            id,
            access,
            speed
        )
    }

    suspend fun deleteClient(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteClientResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteClient("Bearer $token", id)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiServiceCAC: ServiceApiCAC,
            apiServiceMikrotik: ServiceApiMikrotik,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiServiceCAC, apiServiceMikrotik)
            }.also { instance = it }
    }
}