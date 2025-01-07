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
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class ClientDetailVM(private val repository: Repository) : ViewModel() {

    //Delete Client
    private val _deleteClient = MediatorLiveData<Results<DeleteClientResponse>>()
    val deleteClient: LiveData<Results<DeleteClientResponse>> = _deleteClient

    fun deleteClient(id: Int) {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    val bearerToken = "Bearer $token"
                    val source = repository.deleteClient(bearerToken, id)
                    _deleteClient.addSource(source) { result ->
                        _deleteClient.value = result
                        _deleteClient.removeSource(source)
                    }
                }
            }
        }
    }

    //Update Client Access & Speed
    private val _updateClient = MediatorLiveData<Results<UpdateClientDetailResponse>>()
    val updateClient: MutableLiveData<Results<UpdateClientDetailResponse>> = _updateClient

    fun updateClient(id: Int, access: Int, speed: Int) {
        viewModelScope.launch {
            _updateClient.value = Results.Loading
            try {
                repository.getSession().collect { user ->
                    user.token.let { token ->
                        val bearerToken = "Bearer $token"
                        val response = repository.updateClient(bearerToken, id, access, speed)
                        _updateClient.value = Results.Success(response)
                    }
                }
            } catch (e: Exception) {
                _updateClient.value = Results.Error(e.message.toString())
            }
        }
    }

    //Get Client Detail
    private val _getAccess = MediatorLiveData<Results<GetAccessResponse>>()
    val getAccess: MutableLiveData<Results<GetAccessResponse>> = _getAccess

    private val _getSpeed = MediatorLiveData<Results<GetSpeedResponse>>()
    val getSpeed: MutableLiveData<Results<GetSpeedResponse>> = _getSpeed

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

    init {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getAccess.addSource(repository.getAccess(token)) { result ->
                        _getAccess.value = result
                    }
                    _getSpeed.addSource(repository.getSpeed(token)) { result ->
                        _getSpeed.value = result
                    }
                }
            }
        }
    }
}