package com.example.clientaccesscontrol.view.ui.networkfilter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.cacresponse.DeleteBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteModeResponse
import com.example.clientaccesscontrol.data.cacresponse.DeletePresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.GetFilteredClientResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateClientDetailResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class NetworkFilterVM(private val repository: Repository) : ViewModel() {

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

    //Get Filtered Client
    private val _getFilteredClient = MediatorLiveData<Results<GetFilteredClientResponse>>()
    val getFilteredClient: LiveData<Results<GetFilteredClientResponse>> = _getFilteredClient

    fun getFilteredClient(
        btsId: Int,
        radioId: Int,
        modeId: Int,
        channelWidthId: Int,
        preSharedKeyId: Int
    ) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.getFilteredClient(
                        token,
                        btsId,
                        radioId,
                        modeId,
                        channelWidthId,
                        preSharedKeyId
                    )
                    _getFilteredClient.addSource(source) { getFilteredClientResult ->
                        _getFilteredClient.value = getFilteredClientResult
                    }
                }
            }
        }
    }

    private val _deleteBTS = MediatorLiveData<Results<DeleteBTSResponse>>()
    val deleteBTS: LiveData<Results<DeleteBTSResponse>> = _deleteBTS

    fun deleteBTS(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.deleteBTS(token, id)
                    _deleteBTS.addSource(source) { deleteBTSResult ->
                        _deleteBTS.value = deleteBTSResult
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
                    _deleteMode.addSource(source) { deleteModeResult ->
                        _deleteMode.value = deleteModeResult
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
                    _deleteRadio.addSource(source) { deleteRadioResult ->
                        _deleteRadio.value = deleteRadioResult
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
                    _deleteChannelWidth.addSource(source) { deleteChannelWidthResult ->
                        _deleteChannelWidth.value = deleteChannelWidthResult
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
                    _deletePresharedKey.addSource(source) { deletePreSharedKeyResult ->
                        _deletePresharedKey.value = deletePreSharedKeyResult
                        _deletePresharedKey.removeSource(source)
                    }
                }
            }
        }
    }
}