package com.example.clientaccesscontrol.data.retrofit

import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import retrofit2.http.GET

interface ServiceApiMikrotik {

    @GET("queue/tree")
    suspend fun getQueueTree(): List<GetQueueTreeResponseItem>
}