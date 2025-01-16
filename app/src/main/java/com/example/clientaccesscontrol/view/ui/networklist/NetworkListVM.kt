package com.example.clientaccesscontrol.view.ui.networklist

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.cacresponse.CreateBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateModeResponse
import com.example.clientaccesscontrol.data.cacresponse.CreatePresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.GetBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.GetChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.GetModeResponse
import com.example.clientaccesscontrol.data.cacresponse.GetPresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.GetRadioResponse
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class NetworkListVM(private val repository: Repository) : ViewModel() {

    //BTS
    private val _getBTS = MediatorLiveData<Results<GetBTSResponse>>()
    val getBTS: MutableLiveData<Results<GetBTSResponse>> = _getBTS

    fun getBTS() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getBTS.addSource(repository.getBTS(token)) { getBTSResult ->
                        _getBTS.value = getBTSResult
                    }
                }
            }
        }
    }

    private val _createBTS = MediatorLiveData<Results<CreateBTSResponse>>()
    val createBTS: MutableLiveData<Results<CreateBTSResponse>> = _createBTS

    fun createBTS(bts: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createBTS.addSource(repository.createBTS(token, bts)) { createBTSResult ->
                        _createBTS.value = createBTSResult
                    }
                }
            }
        }
    }

    //Radio
    private val _getRadio = MediatorLiveData<Results<GetRadioResponse>>()
    val getRadio: MutableLiveData<Results<GetRadioResponse>> = _getRadio

    fun getRadio() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getRadio.addSource(repository.getRadio(token)) { getRadioResult ->
                        _getRadio.value = getRadioResult
                    }
                }
            }
        }
    }

    private val _createRadio = MediatorLiveData<Results<CreateRadioResponse>>()
    val createRadio: MutableLiveData<Results<CreateRadioResponse>> = _createRadio

    fun createRadio(radio: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createRadio.addSource(repository.createRadio(token, radio)) { createRadioResult ->
                        _createRadio.value = createRadioResult
                    }
                }
            }
        }
    }

    //Mode
    private val _getMode = MediatorLiveData<Results<GetModeResponse>>()
    val getMode: MutableLiveData<Results<GetModeResponse>> = _getMode

    fun getMode() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getMode.addSource(repository.getMode(token)) { getModeResult ->
                        _getMode.value = getModeResult
                    }
                }
            }
        }
    }

    private val _createMode = MediatorLiveData<Results<CreateModeResponse>>()
    val createMode: MutableLiveData<Results<CreateModeResponse>> = _createMode

    fun createMode(mode: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createMode.addSource(repository.createMode(token, mode)) { createModeResult ->
                        _createMode.value = createModeResult
                    }
                }
            }
        }
    }

    //Preshared Key
    private val _getPresharedKey = MediatorLiveData<Results<GetPresharedKeyResponse>>()
    val getPresharedKey: MutableLiveData<Results<GetPresharedKeyResponse>> = _getPresharedKey

    fun getPresharedKey() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getPresharedKey.addSource(repository.getPresharedKey(token)) { getPresharedKeyResult ->
                        _getPresharedKey.value = getPresharedKeyResult
                    }
                }
            }
        }
    }

    private val _createPresharedKey = MediatorLiveData<Results<CreatePresharedKeyResponse>>()
    val createPresharedKey: MutableLiveData<Results<CreatePresharedKeyResponse>> = _createPresharedKey

    fun createPresharedKey(presharedKey: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createPresharedKey.addSource(
                        repository.createPresharedKey(token, presharedKey)) { createPresharedKeyResult ->
                        _createPresharedKey.value = createPresharedKeyResult
                    }
                }
            }
        }
    }

    //Channel Width
    private val _getChanelWidth = MediatorLiveData<Results<GetChannelWidthResponse>>()
    val getChannelWidth: MutableLiveData<Results<GetChannelWidthResponse>> = _getChanelWidth

    fun getChannelWidth() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getChanelWidth.addSource(repository.getChannelWidth(token)) { getChannelWidthResult ->
                        _getChanelWidth.value = getChannelWidthResult
                    }
                }
            }
        }
    }

    private val _createChannelWidth = MediatorLiveData<Results<CreateChannelWidthResponse>>()
    val createChannelWidth: MutableLiveData<Results<CreateChannelWidthResponse>> = _createChannelWidth

    fun createChannelWidth(channelWidth: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createChannelWidth.addSource(
                        repository.createChannelWidth(token, channelWidth)) { createChannelWidthResult ->
                        _createChannelWidth.value = createChannelWidthResult
                    }
                }
            }
        }
    }
}