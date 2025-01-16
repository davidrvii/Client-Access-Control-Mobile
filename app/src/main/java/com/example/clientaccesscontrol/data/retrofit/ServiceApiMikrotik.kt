package com.example.clientaccesscontrol.data.retrofit

import com.example.clientaccesscontrol.data.mikrotikresponse.CreateFilterRulesResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleDownloadResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleLANResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleUploadResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateQueueTreeResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetMangleResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiceApiMikrotik {

    @GET("queue/tree")
    suspend fun getQueueTree(): List<GetQueueTreeResponseItem>

    @Headers("Content-Type: application/json")
    @POST("queue/tree/add")
    suspend fun createQueueTreeUpload(
        @Body requestBody: Map<String, String>
    ): CreateQueueTreeResponse

    @Headers("Content-Type: application/json")
    @POST("queue/tree/add")
    suspend fun createQueueTreeDownload(
        @Body requestBody: Map<String, String>
    ): CreateQueueTreeResponse

    @Headers("Content-Type: application/json")
    @POST("queue/tree/set")
    suspend fun updateQueueTree(
        @Body requestBody: Map<String, String>
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("queue/tree/set")
    suspend fun updateQueueTreeComment(
        @Body requestBody: Map<String, String>
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @DELETE("queue/tree/{id}")
    suspend fun deleteQueueTree(
        @Path("id") id: String
    ): Response<Unit>

    @GET("ip/firewall/mangle")
    suspend fun getMangle(): List<GetMangleResponseItem>

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/mangle/add")
    suspend fun createMangleUpload(
        @Body requestBody: Map<String, String>
    ): CreateMangleUploadResponse

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/mangle/add")
    suspend fun createMangleDownload(
        @Body requestBody: Map<String, String>
    ): CreateMangleDownloadResponse

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/mangle/add")
    suspend fun createMangleLAN(
        @Body requestBody: Map<String, String>
    ): CreateMangleLANResponse

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/mangle/set")
    suspend fun updatedMangleComment(
        @Body body: Map<String, String>
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @DELETE("ip/firewall/mangle/{id}")
    suspend fun deleteMangle(
        @Path("id") id: String
    ): Response<Unit>

    @GET("ip/firewall/filter")
    suspend fun getFilterRules(): List<GetFilterRulesResponseItem>

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/filter/add")
    suspend fun createFilterRules(
        @Body body: Map<String, String>
    ): CreateFilterRulesResponse

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/filter/set")
    suspend fun updateFilterRules(
        @Body body: Map<String, String>
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/filter/set")
    suspend fun updateFilterRulesComment(
        @Body body: Map<String, String>
    ): Response<Unit>

    @Headers("Content-Type: application/json")
    @DELETE("ip/firewall/filter/{id}")
    suspend fun deleteFilterRules(
        @Path("id") id: String
    ): Response<Unit>
}