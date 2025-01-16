package com.example.clientaccesscontrol.view.ui.editrouter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.cacresponse.GetBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.GetChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.GetClientDetailResponse
import com.example.clientaccesscontrol.data.cacresponse.GetModeResponse
import com.example.clientaccesscontrol.data.cacresponse.GetPresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.GetRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateClientDetailResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateNetworkResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetMangleResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class EditRouterVM(private val repository: Repository) : ViewModel() {

    //Update Queue Tree Comment
    private val _updateQueueTreeComment = MediatorLiveData<Results<Unit>>()
    val updateQueueTreeComment: MutableLiveData<Results<Unit>> = _updateQueueTreeComment

    fun updateQueueTreeComment(
        id: String,
        comment: String
    ) {
        viewModelScope.launch {
            _updateQueueTreeComment.addSource(repository.updateQueueTreeComment(id, comment)) { updateQueueTreeCommentResult ->
                _updateQueueTreeComment.value = updateQueueTreeCommentResult
            }
        }
    }

    //Update Mangle Comment
    private val _updateMangleComment = MediatorLiveData<Results<Unit>>()
    val updateMangleComment: MutableLiveData<Results<Unit>> = _updateMangleComment

    fun updateMangleComment(
        id: String,
        comment: String
    ) {
        viewModelScope.launch {
            _updateMangleComment.addSource(repository.updateMangleComment(id, comment)) { updateMangleCommentResult ->
                _updateMangleComment.value = updateMangleCommentResult
            }
        }
    }

    //Update Filter Rules Comment
    private val _updateFilterRulesComment = MediatorLiveData<Results<Unit>>()
    val updateFilterRulesComment: MutableLiveData<Results<Unit>> = _updateFilterRulesComment

    fun updateFilterRulesComment(
        id: String,
        comment: String
    ) {
        viewModelScope.launch {
            _updateFilterRulesComment.addSource(repository.updateFilterRulesComment(id, comment)) { updateFilterRulesCommentResult ->
                _updateFilterRulesComment.value = updateFilterRulesCommentResult
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

    //Get Mangle
    private val _getMangle = MediatorLiveData<Results<List<GetMangleResponseItem>>>()
    val getMangle: LiveData<Results<List<GetMangleResponseItem>>> = _getMangle

    fun getMangle() {
        viewModelScope.launch {
            _getMangle.addSource(repository.getMangle()) { getMangleResult ->
                _getMangle.value = getMangleResult
            }
        }
    }

    //Patch Client
    private val _updateClient = MediatorLiveData<Results<UpdateClientDetailResponse>>()
    val updateClient: MutableLiveData<Results<UpdateClientDetailResponse>> = _updateClient

    fun updateClient(
        id: Int,
        name: String,
        phone: String,
        address: String
    ) {
        viewModelScope.launch {
            _updateClient.value = Results.Loading
            try {
                repository.getSession().collect { user ->
                    user.token.let { token ->
                        val response = repository.updateClient(token, id, name, phone, address)
                        _updateClient.value = Results.Success(response)
                    }
                }
            } catch (e: Exception) {
                _updateClient.value = Results.Error(e.message.toString())
            }
        }
    }

    //Patch Network
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
        bts: Int
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

    //Update Hint Input Field
    private val _getClientDetail = MediatorLiveData<Results<GetClientDetailResponse>>()
    val getClientDetail: MutableLiveData<Results<GetClientDetailResponse>> = _getClientDetail

    fun getClientDetail(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.getClientDetail(token, id)
                    _getClientDetail.addSource(source) { getClientDetailResult ->
                        _getClientDetail.value = getClientDetailResult
                    }
                }
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