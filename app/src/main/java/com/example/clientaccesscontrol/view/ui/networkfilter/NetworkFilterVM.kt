package com.example.clientaccesscontrol.view.ui.networkfilter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.cacresponse.DeleteBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteModeResponse
import com.example.clientaccesscontrol.data.cacresponse.DeletePresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteRadioResponse
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class NetworkFilterVM(private val repository: Repository) : ViewModel() {

    private val _deleteBTS = MediatorLiveData<Results<DeleteBTSResponse>>()
    val deleteBTS: LiveData<Results<DeleteBTSResponse>> = _deleteBTS

    fun deleteBTS(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.deleteBTS(token, id)
                    _deleteBTS.addSource(source) { result ->
                        _deleteBTS.value = result
                        _deleteBTS.removeSource(source)
                    }
                }
            }
        }
    }

    private val _deleteMode = MediatorLiveData<Results<DeleteModeResponse>>()
    val deleteMode: LiveData<Results<DeleteModeResponse>> = _deleteMode

    fun deleteMode(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.deleteMode(token, id)
                    _deleteMode.addSource(source) { result ->
                        _deleteMode.value = result
                        _deleteMode.removeSource(source)
                    }
                }
            }
        }
    }

    private val _deleteRadio = MediatorLiveData<Results<DeleteRadioResponse>>()
    val deleteRadio: LiveData<Results<DeleteRadioResponse>> = _deleteRadio

    fun deleteRadio(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.deleteRadio(token, id)
                    _deleteRadio.addSource(source) { result ->
                        _deleteRadio.value = result
                        _deleteRadio.removeSource(source)
                    }
                }
            }
        }
    }

    private val _deleteChannelWidth = MediatorLiveData<Results<DeleteChannelWidthResponse>>()
    val deleteChannelWidth: LiveData<Results<DeleteChannelWidthResponse>> = _deleteChannelWidth

    fun deleteChannelWidth(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.deleteChannelWidth(token, id)
                    _deleteChannelWidth.addSource(source) { result ->
                        _deleteChannelWidth.value = result
                        _deleteChannelWidth.removeSource(source)
                    }
                }
            }
        }
    }

    private val _deletePresharedKey = MediatorLiveData<Results<DeletePresharedKeyResponse>>()
    val deletePresharedKey: LiveData<Results<DeletePresharedKeyResponse>> = _deletePresharedKey

    fun deletePresharedKey(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.deletePresharedKey(token, id)
                    _deletePresharedKey.addSource(source) { result ->
                        _deletePresharedKey.value = result
                        _deletePresharedKey.removeSource(source)
                    }
                }
            }
        }
    }
}