package com.example.clientaccesscontrol.view.ui.connect

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.cacresponse.LoginResponse
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.cacresponse.RegisterResponse
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class ConnectVM(private val repository: Repository) : ViewModel() {

    private val _loginResponse = MediatorLiveData<Results<LoginResponse>>()
    val loginResponse: MediatorLiveData<Results<LoginResponse>> = _loginResponse

    suspend fun login(ipAddress: String, username: String, password: String) {
        viewModelScope.launch {
            _loginResponse.value = Results.Loading
            try {
                val response = repository.login(ipAddress, username, password)
                _loginResponse.value = Results.Success(response)
            } catch (e: Exception) {
                _loginResponse.value = Results.Error(e.message.toString())
            }
        }
    }

    suspend fun register(username: String, password: String, ipAddress: String): RegisterResponse {
        return repository.register(username, password, ipAddress)
    }
}