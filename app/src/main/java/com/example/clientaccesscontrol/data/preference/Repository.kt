package com.example.clientaccesscontrol.data.preference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.clientaccesscontrol.data.cacresponse.CreateBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateModeResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateNewClientResponse
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
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateFilterRulesResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleDownloadResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleLANResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleUploadResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateQueueTreeResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetMangleResponseItem
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
            userPreference.saveToken(it)
            Log.d("Repository", "Saved Token in Session: $it")
        }
        loginResponse.loginResult?.ipAddress?.let {
            userPreference.saveBaseUrl(it)
            Log.d("Repository", "Saved Base URL in Session: $it")
        }
        loginResponse.loginResult?.let {
            userPreference.saveMikrotikLogin(it.username.toString(), it.password.toString())
            Log.d("Repository", "Saved Username & Password for Mikrotik Login : $username & $password")
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

    //CLIENT ACCESS CONTROL API

    suspend fun createNewClient(
        token: String,
        name: String,
        phone: String,
        address: String,
        accessId: Int,
        speedId: Int,
    ): LiveData<Results<CreateNewClientResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createNewClient("Bearer $token", name, phone, address, accessId, speedId)
            Log.d("Repository", "Create New Client Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create New Client: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun updateSpeed(
        token: String,
        id: Int,
        speed: Int
    ): UpdateClientDetailResponse {
        Log.d("Repository", "Update Speed to $speed with ID $id")
        return apiServiceCAC.updateSpeed("Bearer $token", id, speed)
    }

    suspend fun updateAccess(
        token: String,
        id: Int,
        access: Int
    ): UpdateClientDetailResponse {
        Log.d("Repository", "Update Access to $access with ID $id")
        return apiServiceCAC.updateAccess("Bearer $token", id, access)
    }

    suspend fun getAllClient(
        token: String,
    ): LiveData<Results<GetAllClientResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getAllClient("Bearer $token")
            Log.d("Repository", "Get All Client Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get All Client: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getClientDetail(
        token: String,
        id: Int,
    ): LiveData<Results<GetClientDetailResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getClientDetail("Bearer $token", id)
            Log.d("Repository", "Get Client Detail Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Client Detail: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deleteClient(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteClientResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteClient("Bearer $token", id)
            Log.d("Repository", "Delete Client Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete Client: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
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
        val response = apiServiceCAC.updateNetwork(
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
        Log.d("Repository", "Update Network Response: $response")
        return response
    }

    suspend fun createBTS(
        token: String,
        bts: String,
    ): LiveData<Results<CreateBTSResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createBTS("Bearer $token", bts)
            Log.d("Repository", "Create BTS Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create BTS: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getBTS(
        token: String,
    ): LiveData<Results<GetBTSResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getBTS("Bearer $token")
            Log.d("Repository", "Get BTS Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get BTS: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deleteBTS(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteBTSResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteBTS("Bearer $token", id)
            Log.d("Repository", "Delete BTS Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete BTS: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createRadio(
        token: String,
        radio: String,
    ): LiveData<Results<CreateRadioResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createRadio("Bearer $token", radio)
            Log.d("Repository", "Create Radio Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Radio: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getRadio(
        token: String,
    ): LiveData<Results<GetRadioResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getRadio("Bearer $token")
            Log.d("Repository", "Get Radio Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Radio: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deleteRadio(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteRadioResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteRadio("Bearer $token", id)
            Log.d("Repository", "Delete Radio Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete Radio: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createMode(
        token: String,
        mode: String,
    ): LiveData<Results<CreateModeResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createMode("Bearer $token", mode)
            Log.d("Repository", "Create Mode Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Mode: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getMode(
        token: String,
    ): LiveData<Results<GetModeResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getMode("Bearer $token")
            Log.d("Repository", "Get Mode Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Mode: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deleteMode(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteModeResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteMode("Bearer $token", id)
            Log.d("Repository", "Delete Mode Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete Mode: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createPresharedKey(
        token: String,
        presharedKey: String,
    ): LiveData<Results<CreatePresharedKeyResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createPresharedKey("Bearer $token", presharedKey)
            Log.d("Repository", "Create Preshared Key Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Preshared Key: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getPresharedKey(
        token: String,
    ): LiveData<Results<GetPresharedKeyResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getPresharedKey("Bearer $token")
            Log.d("Repository", "Get Preshared Key Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Preshared Key: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deletePresharedKey(
        token: String,
        id: Int,
    ): LiveData<Results<DeletePresharedKeyResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deletePresharedKey("Bearer $token", id)
            Log.d("Repository", "Delete Preshared Key Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete Preshared Key: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createChannelWidth(
        token: String,
        channelWidth: String,
    ): LiveData<Results<CreateChannelWidthResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.createChannelWidth("Bearer $token", channelWidth)
            Log.d("Repository", "Create Channel Width Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Channel Width: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getChannelWidth(
        token: String,
    ): LiveData<Results<GetChannelWidthResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getChannelWidth("Bearer $token")
            Log.d("Repository", "Get Channel Width Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Channel Width: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deleteChannelWidth(
        token: String,
        id: Int,
    ): LiveData<Results<DeleteChannelWidthResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.deleteChannelWidth("Bearer $token", id)
            Log.d("Repository", "Delete Channel Width Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete Channel Width: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getAccess(
        token: String,
    ): LiveData<Results<GetAccessResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getAccess("Bearer $token")
            Log.d("Repository", "Get Access Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Access: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getSpeed(
        token: String,
    ): LiveData<Results<GetSpeedResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceCAC.getSpeed("Bearer $token")
            Log.d("Repository", "Get Speed Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Speed: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    //MIKROTIK API

    suspend fun createQueueTreeUpload(
        name: String,
        parent: String,
        comment: String,
        packetMark: String,
        limitAt: String,
        maxLimit: String,
        burstTime: String,
        burstThreshold: String,
        burstLimit: String,
    ): LiveData<Results<CreateQueueTreeResponse>> = liveData {
        emit(Results.Loading)
        try {
            val createQueueTreeBody = mapOf(
                "name" to name,
                "parent" to parent,
                "comment" to comment,
                "packet-mark" to packetMark,
                "limit-at" to limitAt,
                "max-limit" to maxLimit,
                "burst-time" to burstTime,
                "burst-threshold" to burstThreshold,
                "burst-limit" to burstLimit
            )
            val response = apiServiceMikrotik.createQueueTreeUpload(createQueueTreeBody)
            Log.d("Repository", "Create Queue Tree Upload Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Queue Tree Upload: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createQueueTreeDownload(
        name: String,
        parent: String,
        comment: String,
        packetMark: String,
        limitAt: String,
        maxLimit: String,
        burstTime: String,
        burstThreshold: String,
        burstLimit: String,
    ): LiveData<Results<CreateQueueTreeResponse>> = liveData {
        emit(Results.Loading)
        try {
            val createQueueTreeBody = mapOf(
                "name" to name,
                "parent" to parent,
                "comment" to comment,
                "packet-mark" to packetMark,
                "limit-at" to limitAt,
                "max-limit" to maxLimit,
                "burst-time" to burstTime,
                "burst-threshold" to burstThreshold,
                "burst-limit" to burstLimit
            )
            val response = apiServiceMikrotik.createQueueTreeDownload(createQueueTreeBody)
            Log.d("Repository", "Create Queue Tree Download Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Queue Download Tree: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun updateQueueTree(
        numbers: String,
        limitAt: String,
        maxLimit: String,
        burstTime: String,
        burstThreshold: String,
        burstLimit: String,
    ): LiveData<Results<Unit>> = liveData {
        emit(Results.Loading)
        try {
            val updateQueueTreeBody = mapOf(
                "numbers" to numbers,
                "limit-at" to limitAt,
                "max-limit" to maxLimit,
                "burst-time" to burstTime,
                "burst-threshold" to burstThreshold,
                "burst-limit" to burstLimit
            )
            val response = apiServiceMikrotik.updateQueueTree(updateQueueTreeBody)
            Log.d("Repository", "Update Queue Tree Response: $response")
            emit(Results.Success(response.body() ?: Unit))
        } catch (e: Exception) {
            Log.e("Repository", "Error Update Queue Tree: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getQueueTree(): LiveData<Results<List<GetQueueTreeResponseItem>>> = liveData {
        emit(Results.Loading)
        Log.d("Repository", "Getting Queue Tree Loading")
        try {
            val response = apiServiceMikrotik.getQueueTree()
            Log.d("Repository", "Get Queue Tree Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Queue Tree: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deleteQueueTree(
        id: String,
    ): LiveData<Results<Unit>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceMikrotik.deleteQueueTree(id)
            Log.d("Repository", "Delete Queue Tree Response: $response")
            emit(Results.Success(response.body() ?: Unit))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete Queue Tree: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createMangleUpload(
        comment: String,
        chain: String,
        srcAddress: String,
        inInterface: String,
        action: String,
        newPacketMark: String,
    ): LiveData<Results<CreateMangleUploadResponse>> = liveData {
        emit(Results.Loading)
        try {
            val createMangleUploadBody = mapOf(
                "comment" to comment,
                "chain" to chain,
                "src-address" to srcAddress,
                "in-interface" to inInterface,
                "action" to action,
                "new-packet-mark" to newPacketMark
            )
            val response = apiServiceMikrotik.createMangleUpload(createMangleUploadBody)
            Log.d("Repository", "Create Mangle Upload Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Mangle Upload: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createMangleDownload(
        comment: String,
        chain: String,
        dstAddress: String,
        outInterface: String,
        action: String,
        newPacketMark: String,
    ): LiveData<Results<CreateMangleDownloadResponse>> = liveData {
        emit(Results.Loading)
        try {
            val createMangleDownloadBody = mapOf(
                "comment" to comment,
                "chain" to chain,
                "dst-address" to dstAddress,
                "out-interface" to outInterface,
                "action" to action,
                "new-packet-mark" to newPacketMark
            )
            val response = apiServiceMikrotik.createMangleDownload(createMangleDownloadBody)
            Log.d("Repository", "Create Mangle Download Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Mangle Download: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createMangleLAN(
        comment: String,
        chain: String,
        dstAddress: String,
        srcAddressList: String,
        action: String,
        newPacketMark: String,
    ): LiveData<Results<CreateMangleLANResponse>> = liveData {
        emit(Results.Loading)
        try {
            val createMangleLANBody = mapOf(
                "comment" to comment,
                "chain" to chain,
                "dst-address" to dstAddress,
                "src-address-list" to srcAddressList,
                "action" to action,
                "new-packet-mark" to newPacketMark
            )
            val response = apiServiceMikrotik.createMangleLAN(createMangleLANBody)
            Log.d("Repository", "Create Mangle LAN Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Mangle LAN: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getMangle(): LiveData<Results<List<GetMangleResponseItem>>> = liveData {
        emit(Results.Loading)
        Log.d("Repository", "Getting Queue Tree Loading")
        try {
            val response = apiServiceMikrotik.getMangle()
            Log.d("Repository", "Get Queue Tree Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Queue Tree: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deleteMangle(
        id: String,
    ): LiveData<Results<Unit>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceMikrotik.deleteMangle(id)
            Log.d("Repository", "Delete Mangle Response: $response")
            emit(Results.Success(response.body() ?: Unit))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete Mangle: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun createFilterRules(
        comment: String,
        chain: String,
        srcAddress: String,
        action: String,
        disabled: String
        ): LiveData<Results<CreateFilterRulesResponse>> = liveData {
        emit(Results.Loading)
        try {
            val body = mapOf(
                "comment" to comment,
                "chain" to chain,
                "src-address" to srcAddress,
                "action" to action,
                "disabled" to disabled
            )
            val response = apiServiceMikrotik.createFilterRules(body)
            Log.d("Repository", "Create Filter Rules Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Create Filter Rules: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun updatedFilterRules(
        id: String,
        disabled: String
    ): LiveData<Results<Unit>> = liveData {
        emit(Results.Loading)
        try {
            val updateFilterRulesBody = mapOf(
                "numbers" to id,
                "disabled" to disabled
            )
            val response = apiServiceMikrotik.updatedFilterRules(updateFilterRulesBody)
            Log.d("Repository", "Updated Filter Rules Response: $response")
            emit(Results.Success(response.body() ?: Unit))
        } catch (e: Exception) {
            Log.e("Repository", "Error Update Filter Rules: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getFilterRules(): LiveData<Results<List<GetFilterRulesResponseItem>>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceMikrotik.getFilterRules()
            Log.d("Repository", "Get Filter Rules Response: $response")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Repository", "Error Get Filter Rules: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun deleteFilterRules(
        id: String,
    ): LiveData<Results<Unit>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiServiceMikrotik.deleteFilterRules(id)
            Log.d("Repository", "Delete Filter Rules Response: $response")
            emit(Results.Success(response.body() ?: Unit))
        } catch (e: Exception) {
            Log.e("Repository", "Error Delete Filter Rules: ${e.localizedMessage}", e)
            emit(Results.Error(e.message ?: "Unknown error occurred"))
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