package com.example.clientaccesscontrol.data.retrofit

import com.example.clientaccesscontrol.data.cacresponse.CreateBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateModeResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateNewClientResponse
import com.example.clientaccesscontrol.data.cacresponse.CreatePresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.CreateRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteClientResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteModeResponse
import com.example.clientaccesscontrol.data.cacresponse.DeletePresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.DeleteRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.GetAccessResponse
import com.example.clientaccesscontrol.data.cacresponse.GetBTSResponse
import com.example.clientaccesscontrol.data.cacresponse.GetChannelWidthResponse
import com.example.clientaccesscontrol.data.cacresponse.GetClientDetailResponse
import com.example.clientaccesscontrol.data.cacresponse.GetFilteredClientResponse
import com.example.clientaccesscontrol.data.cacresponse.GetModeResponse
import com.example.clientaccesscontrol.data.cacresponse.GetPresharedKeyResponse
import com.example.clientaccesscontrol.data.cacresponse.GetRadioResponse
import com.example.clientaccesscontrol.data.cacresponse.GetSearchedClientResponse
import com.example.clientaccesscontrol.data.cacresponse.GetSpeedResponse
import com.example.clientaccesscontrol.data.cacresponse.LoginResponse
import com.example.clientaccesscontrol.data.cacresponse.RegisterResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateClientDetailResponse
import com.example.clientaccesscontrol.data.cacresponse.UpdateNetworkResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApiCAC {
    @FormUrlEncoded
    @POST("user/add")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("ip_address") ipAddress: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("ip_address") ipAddress: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("client/new")
    suspend fun createNewClient(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
        @Field("access_id") accessId: Int,
        @Field("speed_id") speedId: Int,
    ): CreateNewClientResponse

    @FormUrlEncoded
    @POST("bts/add")
    suspend fun createBTS(
        @Header("Authorization") token: String,
        @Field("bts") bts: String,
    ): CreateBTSResponse

    @FormUrlEncoded
    @POST("mode/add")
    suspend fun createMode(
        @Header("Authorization") token: String,
        @Field("mode") mode: String,
    ): CreateModeResponse

    @FormUrlEncoded
    @POST("radio/add")
    suspend fun createRadio(
        @Header("Authorization") token: String,
        @Field("radio") radio: String,
    ): CreateRadioResponse

    @FormUrlEncoded
    @POST("channelWidth/add")
    suspend fun createChannelWidth(
        @Header("Authorization") token: String,
        @Field("channel_width") channelWidth: String,
    ): CreateChannelWidthResponse

    @FormUrlEncoded
    @POST("presharedKey/add")
    suspend fun createPresharedKey(
        @Header("Authorization") token: String,
        @Field("preshared_key") presharedKey: String,
    ): CreatePresharedKeyResponse

    @FormUrlEncoded
    @PATCH("network/{id}")
    suspend fun updateNetwork(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("radio_name") radioName: String,
        @Field("frequency") frequency: String,
        @Field("ip_radio") ipRadio: String,
        @Field("ip_address") ipAddress: String,
        @Field("wlan_mac_address") wlanMacAddress: String,
        @Field("ssid") ssid: String,
        @Field("radio_signal") radioSignal: String,
        @Field("ap_location") apLocation: String,
        @Field("fk_radio_id") radio: Int,
        @Field("fk_mode_id") mode: Int,
        @Field("fk_channel_width_id") channelWidth: Int,
        @Field("fk_preshared_key_id") presharedKey: Int,
        @Field("comment") comment: String,
        @Field("password") password: String,
        @Field("fk_bts_id") bts: Int,
    ): UpdateNetworkResponse

    @FormUrlEncoded
    @PATCH("client/{id}")
    suspend fun updateSpeed(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("fk_speed_id") speed: Int,
    ): UpdateClientDetailResponse

    @FormUrlEncoded
    @PATCH("client/{id}")
    suspend fun updateAccess(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("fk_access_id") access: Int,
    ): UpdateClientDetailResponse

    @FormUrlEncoded
    @PATCH("client/{id}")
    suspend fun updateClient(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
    ): UpdateClientDetailResponse

    @GET("bts/")
    suspend fun getBTS(
        @Header("Authorization") token: String,
    ): GetBTSResponse

    @GET("radio/")
    suspend fun getRadio(
        @Header("Authorization") token: String,
    ): GetRadioResponse

    @GET("mode/")
    suspend fun getMode(
        @Header("Authorization") token: String,
    ): GetModeResponse

    @GET("channelWidth/")
    suspend fun getChannelWidth(
        @Header("Authorization") token: String,
    ): GetChannelWidthResponse

    @GET("presharedKey/")
    suspend fun getPresharedKey(
        @Header("Authorization") token: String,
    ): GetPresharedKeyResponse

    @GET("access/")
    suspend fun getAccess(
        @Header("Authorization") token: String,
    ): GetAccessResponse

    @GET("speed/")
    suspend fun getSpeed(
        @Header("Authorization") token: String,
    ): GetSpeedResponse

    @GET("client/search")
    suspend fun getSearchedClient(
        @Header("Authorization") token: String,
        @Query("query") name: String,
    ): GetSearchedClientResponse

    @GET("client/filter")
    suspend fun getFilteredClient(
        @Header("Authorization") token: String,
        @Query("fk_bts_id") btsId: Int,
        @Query("fk_radio_id") radioId: Int,
        @Query("fk_mode_id") modeId: Int,
        @Query("fk_channel_width_id") channelWidthId: Int,
        @Query("fk_preShared_key_id") preSharedKeyId: Int,
    ): GetFilteredClientResponse

    @GET("client/{id}")
    suspend fun getClientDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): GetClientDetailResponse

    @DELETE("bts/{id}")
    suspend fun deleteBTS(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): DeleteBTSResponse

    @DELETE("mode/{id}")
    suspend fun deleteMode(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): DeleteModeResponse

    @DELETE("radio/{id}")
    suspend fun deleteRadio(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): DeleteRadioResponse

    @DELETE("channelWidth/{id}")
    suspend fun deleteChannelWidth(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): DeleteChannelWidthResponse

    @DELETE("presharedKey/{id}")
    suspend fun deletePresharedKey(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): DeletePresharedKeyResponse

    @DELETE("client/{id}")
    suspend fun deleteClient(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): DeleteClientResponse
}