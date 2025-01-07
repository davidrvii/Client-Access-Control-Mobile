package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class CreatePresharedKeyResponse(

	@field:SerializedName("newPresharedKey")
	val newPresharedKey: NewPresharedKey? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class NewPresharedKey(

	@field:SerializedName("presharedKey")
	val presharedKey: String? = null
)
