package com.example.clientaccesscontrol.view.ui.connect

import androidx.lifecycle.ViewModel
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.cacresponse.RegisterResponse

class ConnectVM(private val repository: Repository) : ViewModel() {

    suspend fun login(ipAddress: String, username: String, password: String) {
        repository.login(ipAddress, username, password)
    }

    suspend fun register(username: String, password: String, ipAddress: String): RegisterResponse {
        return repository.register(username, password, ipAddress)

    }
}