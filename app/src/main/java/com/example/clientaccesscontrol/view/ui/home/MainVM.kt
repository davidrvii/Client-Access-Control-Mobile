package com.example.clientaccesscontrol.view.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.cacresponse.GetAllClientResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateClientDetailResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.preference.UserModel
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class MainVM(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private val _getQueueTree = MediatorLiveData<Results<List<GetQueueTreeResponseItem>>>()
    val getQueueTree: LiveData<Results<List<GetQueueTreeResponseItem>>> = _getQueueTree

    fun getQueueTree() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getQueueTree.addSource(repository.getQueueTree()) { result ->
                        _getQueueTree.value = result
                    }
                }
            }
        }
    }

    private val _getAllClient = MediatorLiveData<Results<GetAllClientResponse>>()
    val getAllClient: LiveData<Results<GetAllClientResponse>> = _getAllClient

    fun getAllClient() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                user.token.let { token ->
                    _getAllClient.addSource(repository.getAllClient(token)) { result ->
                        _getAllClient.value = result
                    }
                }
            }
        }
    }

    private val _updateClient = MediatorLiveData<Results<UpdateClientDetailResponse>>()
    val updateClient: MutableLiveData<Results<UpdateClientDetailResponse>> = _updateClient

    fun updateClient(id: Int, access: Int, speed: Int) {
        Log.d("MainVM", "Updating client $id with access $access and speed $speed")
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

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}