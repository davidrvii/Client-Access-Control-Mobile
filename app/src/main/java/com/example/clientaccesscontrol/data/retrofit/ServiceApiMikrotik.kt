package com.example.clientaccesscontrol.data.retrofit

import com.example.clientaccesscontrol.data.mikrotikresponse.CreateFilterRulesResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleDownloadResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleLANResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateMangleUploadResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.CreateQueueTreeResponse
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.UpdateFilterRulesResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceApiMikrotik {

    @GET("queue/tree")
    suspend fun getQueueTree(): List<GetQueueTreeResponseItem>

    @GET("ip/firewall/filter")
    suspend fun getFilterRules(): List<GetFilterRulesResponseItem>

    @Headers("Content-Type: application/json")
    @POST("queue/tree/add")
    suspend fun createQueueTree(
        @Field ("name") name: String,
        @Field ("parent") parent: String,
        @Field ("comment") comment: String,
        @Field ("packet-mark") packetMark: String,
        @Field ("limit-at") limitAt: String,
        @Field ("max-limit") maxLimit: String,
        @Field ("burst-time") burstTime: String,
        @Field ("burst-threshold") burstThreshold: String,
        @Field ("burst-limit") burstLimit: String,
    ): CreateQueueTreeResponse

    @Headers("Content-Type: application/json")
    @POST("/ip/firewall/mangle/add")
    suspend fun createMangleUpload(
        @Field("comment") comment: String,
        @Field("chain") chain: String,
        @Field("src-address") srcAddress: String,
        @Field("in-interface") inInterface: String,
        @Field("action") action: String,
        @Field("new-packet-mark") newPacketMark: String,
    ): CreateMangleUploadResponse

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/mangle/add")
    suspend fun createMangleDownload(
        @Field("comment") comment: String,
        @Field("chain") chain: String,
        @Field("dst-address") dstAddress: String,
        @Field("out-interface") outInterface: String,
        @Field("action") action: String,
        @Field("new-packet-mark") newPacketMark: String,
    ): CreateMangleDownloadResponse

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/mangle/add")
    suspend fun createMangleLAN(
        @Field("comment") comment: String,
        @Field("chain") chain: String,
        @Field("dst-address") dstAddress: String,
        @Field("src-address-list") srcAddressList: String,
        @Field("action") action: String,
        @Field("new-packet-mark") newPacketMark: String,
    ): CreateMangleLANResponse

    @Headers("Content-Type: application/json")
    @POST("ip/firewall/filter/add")
    suspend fun createFilterRules(
        @Body body: Map<String, String>
    ): CreateFilterRulesResponse

    @Headers("Content-Type: application/json")
    @PUT("ip/firewall/filter/{id}")
    suspend fun updatedFilterRules(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): UpdateFilterRulesResponse
}