package com.example.clientaccesscontrol.view.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clientaccesscontrol.data.preference.Repository
import com.example.clientaccesscontrol.injection.Data
import com.example.clientaccesscontrol.view.ui.clientdetail.ClientDetailVM
import com.example.clientaccesscontrol.view.ui.connect.ConnectVM
import com.example.clientaccesscontrol.view.ui.editrouter.EditRouterVM
import com.example.clientaccesscontrol.view.ui.filter.FilterBottomSheetVM
import com.example.clientaccesscontrol.view.ui.home.MainVM
import com.example.clientaccesscontrol.view.ui.networkfilter.NetworkFilterVM
import com.example.clientaccesscontrol.view.ui.networklist.NetworkListVM
import com.example.clientaccesscontrol.view.ui.newclientprofile.NewClientProfileVM
import com.example.clientaccesscontrol.view.ui.newclientqueue.NewClientQueueVM
import com.example.clientaccesscontrol.view.ui.newclientrouter.NewClientRouterVM

class FactoryViewModel(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ConnectVM::class.java) -> {
                ConnectVM(repository) as T
            }

            modelClass.isAssignableFrom(MainVM::class.java) -> {
                MainVM(repository) as T
            }

            modelClass.isAssignableFrom(ClientDetailVM::class.java) -> {
                ClientDetailVM(repository) as T
            }

            modelClass.isAssignableFrom(EditRouterVM::class.java) -> {
                EditRouterVM(repository) as T
            }

            modelClass.isAssignableFrom(NewClientProfileVM::class.java) -> {
                NewClientProfileVM(repository) as T
            }

            modelClass.isAssignableFrom(NewClientQueueVM::class.java) -> {
                NewClientQueueVM(repository) as T
            }

            modelClass.isAssignableFrom(NewClientRouterVM::class.java) -> {
                NewClientRouterVM(repository) as T
            }

            modelClass.isAssignableFrom(FilterBottomSheetVM::class.java) -> {
                FilterBottomSheetVM(repository) as T

            }

            modelClass.isAssignableFrom(NetworkListVM::class.java) -> {
                NetworkListVM(repository) as T
            }

            modelClass.isAssignableFrom(NetworkFilterVM::class.java) -> {
                NetworkFilterVM(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)

        }
    }

    companion object {
        @Volatile
        private var instance: FactoryViewModel? = null
        fun getInstance(context: Context): FactoryViewModel =
            instance ?: synchronized(FactoryViewModel::class.java) {
                instance ?: FactoryViewModel(Data.provideCACRepository(context))
            }.also {
                instance = it
            }
    }

}