package com.example.clientaccesscontrol.view.ui.editrouter

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
import com.example.clientaccesscontrol.data.cacresponse.UpdateNetworkResponse
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class EditRouterVM(private val repository: Repository) : ViewModel() {

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
                    _getClientDetail.addSource(source) { result ->
                        _getClientDetail.value = result
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
                    _getBTS.addSource(repository.getBTS(token)) { result ->
                        _getBTS.value = result
                    }
                    _getRadio.addSource(repository.getRadio(token)) { result ->
                        _getRadio.value = result
                    }
                    _getMode.addSource(repository.getMode(token)) { result ->
                        _getMode.value = result
                    }
                    _getPresharedKey.addSource(repository.getPresharedKey(token)) { result ->
                        _getPresharedKey.value = result
                    }
                    _getChanelWidth.addSource(repository.getChannelWidth(token)) { result ->
                        _getChanelWidth.value = result
                    }
                }
            }
        }
    }
}