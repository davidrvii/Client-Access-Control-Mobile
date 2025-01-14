package com.example.clientaccesscontrol.view.ui.newclientprofile

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientaccesscontrol.data.cacresponse.GetAccessResponse
import com.example.clientaccesscontrol.data.cacresponse.GetSpeedResponse
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.data.result.Results
import kotlinx.coroutines.launch

class NewClientProfileVM(private val repository: Repository) : ViewModel() {

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