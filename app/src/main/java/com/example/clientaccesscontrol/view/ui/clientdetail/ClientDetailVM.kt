package com.example.clientaccesscontrol.view.ui.clientdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.cacresponse.DeleteClientResponse
import com.example.clientaccesscontrol.data.cacresponse.GetAccessResponse
import com.example.clientaccesscontrol.data.cacresponse.GetClientDetailResponse
import com.example.clientaccesscontrol.data.cacresponse.GetSpeedResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateClientDetailResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateFilterRulesResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetMangleResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class ClientDetailVM(private val repository: Repository) : ViewModel() {

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

    //Update Queue Tree Speed
    private val _updateQueueTreeSpeed = MediatorLiveData<Results<Unit>>()
    val updateQueueTreeSpeed: LiveData<Results<Unit>> = _updateQueueTreeSpeed

    fun updateQueueTreeSpeed(
        numbers: String,
        limitAt: String,
        maxLimit: String,
        burstTime: String,
        burstThreshold: String,
        burstLimit: String,
    ) {
        viewModelScope.launch {
            _updateQueueTreeSpeed.addSource(
                repository.updateQueueTree(numbers, limitAt, maxLimit, burstTime, burstThreshold, burstLimit)) { updateQueueTreeSpeedResult ->
                _updateQueueTreeSpeed.value = updateQueueTreeSpeedResult
            }
        }
    }

    //Delete Queue Tree
    private val _deleteQueueTree = MediatorLiveData<Results<Unit>>()
    val deleteQueueTree: LiveData<Results<Unit>> = _deleteQueueTree

    fun deleteQueueTree(id: String) {
        viewModelScope.launch {
            _deleteQueueTree.addSource(
                repository.deleteQueueTree(id)) { deleteQueueTreeResult ->
                _deleteQueueTree.value = deleteQueueTreeResult
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
        disabled: String,
    ) {
        viewModelScope.launch {
            _createFilterRules.addSource(
                repository.createFilterRules(comment, chain, srcAddress, action, disabled)) { createFilterRulesResult ->
                _createFilterRules.value = createFilterRulesResult
            }
        }
    }

    //Update Filter Rules
    private val _updateFilterRules = MediatorLiveData<Results<Unit>>()
    val updateFilterRules: LiveData<Results<Unit>> = _updateFilterRules

    fun updateFilterRules(
        id: String,
        disabled: String,
    ) {
        viewModelScope.launch {
            _updateFilterRules.addSource(
                repository.updatedFilterRules(id, disabled)) { updateFilterRulesResult ->
                _updateFilterRules.value = updateFilterRulesResult
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

    //Delete Filter Rules
    private val _deleteFilterRules = MediatorLiveData<Results<Unit>>()
    val deleteFilterRules: LiveData<Results<Unit>> = _deleteFilterRules

    fun deleteFilterRules(id: String) {
        viewModelScope.launch {
            _deleteFilterRules.addSource(
                repository.deleteFilterRules(id)) { deleteFilterRulesResult ->
                _deleteFilterRules.value = deleteFilterRulesResult
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

    //Delete Mangle
    private val _deleteMangle = MediatorLiveData<Results<Unit>>()
    val deleteMangle: LiveData<Results<Unit>> = _deleteMangle

    fun deleteMangle(id: String) {
        viewModelScope.launch {
            _deleteMangle.addSource(
                repository.deleteMangle(id)) { deleteMangleResult ->
                _deleteMangle.value = deleteMangleResult
            }
        }
    }

    //Delete Client
    private val _deleteClient = MediatorLiveData<Results<DeleteClientResponse>>()
    val deleteClient: LiveData<Results<DeleteClientResponse>> = _deleteClient

    fun deleteClient(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val source = repository.deleteClient(token, id)
                    _deleteClient.removeSource(source)
                    _deleteClient.addSource(source) { deleteClientResult ->
                        _deleteClient.value = deleteClientResult
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

    //Update Client Speed
    private val _updateSpeed = MediatorLiveData<Results<UpdateClientDetailResponse>>()
    val updateSpeed: MutableLiveData<Results<UpdateClientDetailResponse>> = _updateSpeed

    fun updateSpeed(id: Int, speed: Int) {
        viewModelScope.launch {
            _updateSpeed.value = Results.Loading
            try {
                repository.getSession().collect { user ->
                    user.token.let { token ->
                        val response = repository.updateSpeed(token, id, speed)
                        _updateSpeed.value = Results.Success(response)
                    }
                }
            } catch (e: Exception) {
                _updateSpeed.value = Results.Error(e.message.toString())
            }
        }
    }

    //Get Client Detail
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

    //Setup Spinner
    private val _getAccess = MediatorLiveData<Results<GetAccessResponse>>()
    val getAccess: MutableLiveData<Results<GetAccessResponse>> = _getAccess

    private val _getSpeed = MediatorLiveData<Results<GetSpeedResponse>>()
    val getSpeed: MutableLiveData<Results<GetSpeedResponse>> = _getSpeed

    init {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getAccess.addSource(repository.getAccess(token)) { getAccessResult ->
                        _getAccess.value = getAccessResult
                    }
                    _getSpeed.addSource(repository.getSpeed(token)) { getSpeedResult ->
                        _getSpeed.value = getSpeedResult
                    }
                }
            }
        }
    }
}