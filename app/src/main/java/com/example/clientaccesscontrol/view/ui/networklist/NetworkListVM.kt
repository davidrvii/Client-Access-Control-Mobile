package com.example.clientaccesscontrol.view.ui.networklist

import android.util.Log
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

    private val _createBTS = MediatorLiveData<Results<CreateBTSResponse>>()
    val createBTS: MutableLiveData<Results<CreateBTSResponse>> = _createBTS

    fun createBTS(bts: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createBTS.addSource(repository.createBTS(token, bts)) { result ->
                        _createBTS.value = result
                        Log.d("NetworkListVM", "createBTS result: $result")
                    }
                }
            }
        }
    }

    //Radio
    private val _getRadio = MediatorLiveData<Results<GetRadioResponse>>()
    val getRadio: MutableLiveData<Results<GetRadioResponse>> = _getRadio

    private val _createRadio = MediatorLiveData<Results<CreateRadioResponse>>()
    val createRadio: MutableLiveData<Results<CreateRadioResponse>> = _createRadio

    fun createRadio(radio: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createRadio.addSource(repository.createRadio(token, radio)) { result ->
                        _createRadio.value = result
                    }
                }
            }
        }
    }

    //Mode
    private val _getMode = MediatorLiveData<Results<GetModeResponse>>()
    val getMode: MutableLiveData<Results<GetModeResponse>> = _getMode

    private val _createMode = MediatorLiveData<Results<CreateModeResponse>>()
    val createMode: MutableLiveData<Results<CreateModeResponse>> = _createMode

    fun createMode(mode: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createMode.addSource(repository.createMode(token, mode)) { result ->
                        _createMode.value = result
                    }
                }
            }
        }
    }

    //Preshared Key
    private val _getPresharedKey = MediatorLiveData<Results<GetPresharedKeyResponse>>()
    val getPresharedKey: MutableLiveData<Results<GetPresharedKeyResponse>> = _getPresharedKey

    private val _createPresharedKey = MediatorLiveData<Results<CreatePresharedKeyResponse>>()
    val createPresharedKey: MutableLiveData<Results<CreatePresharedKeyResponse>> =
        _createPresharedKey

    fun createPresharedKey(presharedKey: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createPresharedKey.addSource(
                        repository.createPresharedKey(
                            token,
                            presharedKey
                        )
                    ) { result ->
                        _createPresharedKey.value = result
                    }
                }
            }
        }
    }

    //Channel Width
    private val _getChanelWidth = MediatorLiveData<Results<GetChannelWidthResponse>>()
    val getChannelWidth: MutableLiveData<Results<GetChannelWidthResponse>> = _getChanelWidth

    private val _createChannelWidth = MediatorLiveData<Results<CreateChannelWidthResponse>>()
    val createChannelWidth: MutableLiveData<Results<CreateChannelWidthResponse>> =
        _createChannelWidth

    fun createChannelWidth(channelWidth: String) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _createChannelWidth.addSource(
                        repository.createChannelWidth(
                            token,
                            channelWidth
                        )
                    ) { result ->
                        _createChannelWidth.value = result
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    //get
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