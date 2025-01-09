package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class CreateNewClientResponse(

	@field:SerializedName("newClient")
	val newClient: NewClient? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class NewClient(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("clientId")
	val clientId: Int? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("access_id")
	val accessId: Int? = null,

	@field:SerializedName("speed_id")
	val speedId: Int? = null
)
