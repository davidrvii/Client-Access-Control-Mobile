package com.example.clientaccesscontrol.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.cacresponse.CreateNewClientResponse
import com.example.clientaccesscontrol.data.cacresponse.GetAllClientResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateClientDetailResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateNetworkResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.preference.UserModel
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class MainVM(private val repository: Repository) : ViewModel() {

    //Check Login Session
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    //Get Filter Rules
    private val _getFilterRules = MediatorLiveData<Results<List<GetFilterRulesResponseItem>>>()
    val getFilterRules: LiveData<Results<List<GetFilterRulesResponseItem>>> = _getFilterRules

    fun getFilterRules() {
        viewModelScope.launch {
            _getFilterRules.addSource(repository.getFilterRules()) { getFilterRulesResult ->
                _getFilterRules.value = getFilterRulesResult
            }
        }
    }

    //Create New Client
    private val _createNewClient = MediatorLiveData<Results<CreateNewClientResponse>>()
    val createNewClient: MutableLiveData<Results<CreateNewClientResponse>> = _createNewClient

    fun createNewClient(
        name: String,
        phone: String,
        address: String,
        accessId: Int,
        speedId: Int,
    ) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createNewClient.addSource(
                        repository.createNewClient(token, name, phone, address, accessId, speedId)) { createNewClientResult ->
                        _createNewClient.value = createNewClientResult
                    }
                }
            }
        }
    }

    //Create New Network
    private val _updateNetwork = MediatorLiveData<Results<UpdateNetworkResponse>>()
    val updateNetwork: MutableLiveData<Results<UpdateNetworkResponse>> = _updateNetwork

    fun updateNetwork(
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
    ) {
        viewModelScope.launch {
            _updateNetwork.value = Results.Loading
            try {
                repository.getSession().collect { user ->
                    user.token.let { token ->
                        val response = repository.updateNetwork(
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
                        _updateNetwork.value = Results.Success(response)
                    }
                }
            } catch (e: Exception) {
                _updateNetwork.value = Results.Error(e.message.toString())
            }
        }
    }

    //Get Queue Tree
    private val _getQueueTree = MediatorLiveData<Results<List<GetQueueTreeResponseItem>>>()
    val getQueueTree: LiveData<Results<List<GetQueueTreeResponseItem>>> = _getQueueTree

    fun getQueueTree() {
        viewModelScope.launch {
            _getQueueTree.addSource(repository.getQueueTree()) { getQueueTreeResult ->
                _getQueueTree.value = getQueueTreeResult
            }
        }
    }

    //Get All Client
    private val _getAllClient = MediatorLiveData<Results<GetAllClientResponse>>()
    val getAllClient: LiveData<Results<GetAllClientResponse>> = _getAllClient

    fun getAllClient() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getAllClient.addSource(repository.getAllClient(token)) { getAllClientResult ->
                        _getAllClient.value = getAllClientResult
                    }
                }
            }
        }
    }

    //Update Client Access
    private val _updateAccess = MediatorLiveData<Results<UpdateClientDetailResponse>>()
    val updateAccess: MutableLiveData<Results<UpdateClientDetailResponse>> = _updateAccess

    fun updateAccess(id: Int, access: Int) {
        viewModelScope.launch {
            _updateAccess.value = Results.Loading
            try {
                repository.getSession().collect { user ->
                    user.token.let { token ->
                        val response = repository.updateAccess(token, id, access)
                        _updateAccess.value = Results.Success(response)
                    }
                }
            } catch (e: Exception) {
                _updateAccess.value = Results.Error(e.message.toString())
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}