package com.example.clientaccesscontrol.view.ui.newclientrouter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.cacresponse.CreateNewClientResponse
import com.example.clientaccesscontrol.data.cacresponse.GetBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.GetChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.GetModeResponse
import com.example.clientaccesscontrol.data.cacresponse.GetPresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.GetRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateNetworkResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateFilterRulesResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleDownloadResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleLANResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleUploadResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateQueueTreeResponse
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class NewClientRouterVM(private val repository: Repository) : ViewModel() {

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

    //Create Mangle Upload
    private val _createMangleUpload = MediatorLiveData<Results<CreateMangleUploadResponse>>()
    val createMangleUpload: MutableLiveData<Results<CreateMangleUploadResponse>> = _createMangleUpload

    fun createMangleUpload(
        comment: String,
        chain: String,
        srcAddress: String,
        inInterface: String,
        action: String,
        newPacketMark: String
    ) {
        viewModelScope.launch {
            _createMangleUpload.addSource(repository.createMangleUpload(comment, chain, srcAddress, inInterface, action, newPacketMark)) { createMangleUploadRules ->
                _createMangleUpload.value = createMangleUploadRules
            }
        }
    }

    //Create Mangle Download
    private val _createMangleDownload = MediatorLiveData<Results<CreateMangleDownloadResponse>>()
    val createMangleDownload: MutableLiveData<Results<CreateMangleDownloadResponse>> = _createMangleDownload

    fun createMangleDownload(
        comment: String,
        chain: String,
        dstAddress: String,
        outInterface: String,
        action: String,
        newPacketMark: String
    ) {
        viewModelScope.launch {
            _createMangleDownload.addSource(repository.createMangleDownload(comment, chain, dstAddress, outInterface, action, newPacketMark)) { createMangleDownloadRules ->
                _createMangleDownload.value = createMangleDownloadRules
            }
        }
    }

    //Create Mangle LAN
    private val _createMangleLAN = MediatorLiveData<Results<CreateMangleLANResponse>>()
    val createMangleLAN: MutableLiveData<Results<CreateMangleLANResponse>> = _createMangleLAN

    fun createMangleLAN(
        comment: String,
        chain: String,
        dstAddress: String,
        srcAddressList: String,
        action: String,
        newPacketMark: String
    ) {
        viewModelScope.launch {
            _createMangleLAN.addSource(repository.createMangleLAN(comment, chain, dstAddress, srcAddressList, action, newPacketMark)) { createMangleLANRules ->
                _createMangleLAN.value = createMangleLANRules
            }
        }
    }

    //Create Queue Tree
    private val _createQueueTree = MediatorLiveData<Results<CreateQueueTreeResponse>>()
    val createQueueTree: MutableLiveData<Results<CreateQueueTreeResponse>> = _createQueueTree

    fun createQueueTree(
        name: String,
        parent: String,
        comment: String,
        packetMark: String,
        limitAt: String,
        maxLimit: String,
        burstTime: String,
        burstThreshold: String,
        burstLimit: String,
    ) {
        viewModelScope.launch {
            _createQueueTree.addSource(repository.createQueueTree(name, parent, comment, packetMark, limitAt, maxLimit, burstTime, burstThreshold, burstLimit)) { createQueueTreeRules ->
                _createQueueTree.value = createQueueTreeRules
            }
        }
    }

    //Create Filter Rules
    private val _createFilterRules = MediatorLiveData<Results<CreateFilterRulesResponse>>()
    val createFilterRules: LiveData<Results<CreateFilterRulesResponse>> = _createFilterRules

    fun createFilterRules(
        comment: String,
        chain: String,
        srcAddress: String,
        action: String,
        disabled: String
    ) {
        viewModelScope.launch {
            _createFilterRules.addSource(repository.createFilterRules(comment, chain, srcAddress, action, disabled)) { createFilterRulesResult ->
                _createFilterRules.value = createFilterRulesResult
            }
        }
    }

    //BTS
    private val _getBTS = MediatorLiveData<Results<GetBTSResponse>>()
    val getBTS: MutableLiveData<Results<GetBTSResponse>> = _getBTS

    //Radio
    private val _getRadio = MediatorLiveData<Results<GetRadioResponse>>()
    val getRadio: MutableLiveData<Results<GetRadioResponse>> = _getRadio

    //Mode
    private val _getMode = MediatorLiveData<Results<GetModeResponse>>()
    val getMode: MutableLiveData<Results<GetModeResponse>> = _getMode

    //Preshared Key
    private val _getPresharedKey = MediatorLiveData<Results<GetPresharedKeyResponse>>()
    val getPresharedKey: MutableLiveData<Results<GetPresharedKeyResponse>> = _getPresharedKey

    //Channel Width
    private val _getChanelWidth = MediatorLiveData<Results<GetChannelWidthResponse>>()
    val getChannelWidth: MutableLiveData<Results<GetChannelWidthResponse>> = _getChanelWidth

    init {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getBTS.addSource(repository.getBTS(token)) { getBTSResult ->
                        _getBTS.value = getBTSResult
                    }
                    _getRadio.addSource(repository.getRadio(token)) { getRadioResult ->
                        _getRadio.value = getRadioResult
                    }
                    _getMode.addSource(repository.getMode(token)) { getModeResult ->
                        _getMode.value = getModeResult
                    }
                    _getPresharedKey.addSource(repository.getPresharedKey(token)) { getPresharedKeyResult ->
                        _getPresharedKey.value = getPresharedKeyResult
                    }
                    _getChanelWidth.addSource(repository.getChannelWidth(token)) { getChannelWidthResult ->
                        _getChanelWidth.value = getChannelWidthResult
                    }
                }
            }
        }
    }
}