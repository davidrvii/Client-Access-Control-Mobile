package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class GetAllClientResponse(

	@field:SerializedName("clients")
	val clients: List<ClientsItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class ClientsItem(

	@field:SerializedName("internet_access")
	val internetAccess: String? = null,

	@field:SerializedName("speed_id")
	val speedId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ip_address")
	val ipAddress: String? = null,

	@field:SerializedName("client_id")
	val clientId: Int? = null,

	@field:SerializedName("comment")
	val comment: String? = null
)
